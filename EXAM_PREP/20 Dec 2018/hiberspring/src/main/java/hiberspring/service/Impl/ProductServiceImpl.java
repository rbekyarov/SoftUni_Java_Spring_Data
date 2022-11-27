package hiberspring.service.Impl;

import hiberspring.domain.dtos.ImportProductDto;
import hiberspring.domain.dtos.ImportProductRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Product;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.ProductRepository;
import hiberspring.service.ProductService;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final static String PRODUCTS_FILE_PATH = "src/main/resources/files/products.xml";
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public ProductServiceImpl(ProductRepository productRepository, BranchRepository branchRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Boolean productsAreImported() {
        return productRepository.count()>0;
    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return Files
                .readString(Path.of(PRODUCTS_FILE_PATH));
    }

    @Override
    public String importProducts() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        if (productRepository.count() == 0) {
            List<ImportProductDto> productDtoList =
                    xmlParser.fromFile(PRODUCTS_FILE_PATH, ImportProductRootDto.class)
                            .getImportProductDtos();

            for (ImportProductDto productDto : productDtoList) {
                boolean isValid = validationUtil.isValid(productDto);
                Product product = modelMapper.map(productDto, Product.class);

                Optional<Branch> branch = branchRepository.findByName(productDto.getBranch());

                if (branch.isEmpty() || !isValid) {
                    sb.append("Invalid product\n");
                } else {


                    product.setBranch(branch.get());

                    productRepository.save(product);
                    sb.append(String.format("Successfully imported Product %s.\n", product.getName()));
                }
            }
        }

        return sb.toString();
    }
}
