package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.ImportUserDto;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.models.entity.Post;
import softuni.exam.instagraphlite.models.entity.User;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final String USERS_FILE_PATH = "src/main/resources/files/users.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private UserRepository userRepository;
    private PictureRepository pictureRepository;
    private PostRepository postRepository;

    public UserServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, UserRepository userRepository, PictureRepository pictureRepository, PostRepository postRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
        this.postRepository = postRepository;
    }

    @Override
    public boolean areImported() {
        return userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(USERS_FILE_PATH));
    }

    @Override
    public String importUsers() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (userRepository.count() == 0) {

            ImportUserDto[] importUserDtos = gson.fromJson(readFromFileContent(), ImportUserDto[].class);
            for (ImportUserDto userDto : importUserDtos) {
                boolean isValid = validationUtil.isValid(userDto);
                // проверка дали вече съществува
                User user = modelMapper.map(userDto, User.class);

                Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());

                if (userByUsername.isPresent() || !isValid) {
                    sb.append("Invalid User\n");
                } else {
                    //проверка по town name
                    Optional<Picture> pictureByPath = pictureRepository.findByPath(userDto.getProfilePicture());
                    if (pictureByPath.isEmpty()) {
                        sb.append("Invalid User\n");

                    } else {
                        user.setUsername(userDto.getUsername());
                        user.setPassword(userDto.getPassword());
                        user.setProfilePicture(pictureByPath.get());

                        userRepository.save(user);
                        sb.append(String.format("Successfully imported User: %s \n", user.getUsername()));
                    }

                }
            }
        }

        return sb.toString();
    }


    @Override
    public String exportUsersWithTheirPosts() {
        StringBuilder sb = new StringBuilder();
        List<Post> posts = postRepository.findAll();


        for (Post post : posts) {

            String username = post.getUser().getUsername();
            sb.append(String.format("User: %s\n", username));
            List<Post> allByUser_username = postRepository.findAllByUser_Username(username);
            sb.append(String.format("Post count: %d\n", allByUser_username.size()));
            sb.append("==Post Details:");
            for (Post postUser : allByUser_username) {
                String caption = postUser.getCaption();
                Float size = postUser.getPicture().getSize();
                sb.append(String.format("----Caption: %s\n", caption));
                sb.append(String.format("----Picture Size: %.2f\n", size));
            }


        }

        return sb.toString();
    }
}
