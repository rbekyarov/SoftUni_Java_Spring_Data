package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.ImportPostDto;
import softuni.exam.instagraphlite.models.dto.ImportPostRootDto;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.models.entity.Post;
import softuni.exam.instagraphlite.models.entity.User;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.util.ValidationUtil;
import softuni.exam.instagraphlite.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private final static String POSTS_FILE_PATH = "src/main/resources/files/posts.xml";
    private final PostRepository postRepository;
    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public PostServiceImpl(PostRepository postRepository, PictureRepository pictureRepository, UserRepository userRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.postRepository = postRepository;
        this.pictureRepository = pictureRepository;
        this.userRepository = userRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return postRepository.count()>0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(POSTS_FILE_PATH));
    }

    @Override
    public String importPosts() throws IOException, JAXBException {

            StringBuilder sb = new StringBuilder();
            if (postRepository.count() == 0) {
                ImportPostRootDto importPostRootDto = xmlParser
                        .fromFile(POSTS_FILE_PATH, ImportPostRootDto.class);
                List<ImportPostDto> importPostDtos = importPostRootDto.getImportPostDtos();
                for (ImportPostDto postDto : importPostDtos) {

                    boolean isValid = validationUtil.isValid(postDto);

                    if (userRepository.findByUsername(postDto.getUser().getUsername()).isEmpty()) {
                        isValid = false;
                    }
                    if (pictureRepository.findByPath(postDto.getPicture().getPath()).isEmpty()) {
                        isValid = false;
                    }
                    Optional<Post> byCaption = postRepository.findByCaption(postDto.getCaption());
                    if (byCaption.isPresent()) {
                        isValid = false;
                    }

                    if (!isValid) {
                        sb.append("Invalid Post\n");
                    } else {
                        Post post = modelMapper.map(postDto, Post.class);
                        post.setCaption(postDto.getCaption());

                        Optional<User> userByName = userRepository.findByUsername(postDto.getUser().getUsername());
                        post.setUser(userByName.get());

                        Optional<Picture> pictureByPath = pictureRepository.findByPath(postDto.getPicture().getPath());
                        post.setPicture(pictureByPath.get());

                        postRepository.save(post);
                        sb.append(String.format("Successfully imported Post, made by %s\n", post.getUser().getUsername()));
                    }
                }
            }

            return sb.toString();
    }
}
