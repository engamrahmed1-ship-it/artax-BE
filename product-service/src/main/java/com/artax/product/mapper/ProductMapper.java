package com.artax.product.mapper;

import com.artax.product.Dto.ProductRequest;
import com.artax.product.Dto.ProductResponse;
import com.artax.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

//@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,imports = java.math.BigDecimal.class)
@Mapper(componentModel = "spring" ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,imports = java.math.BigDecimal.class) // This is the key!
public interface ProductMapper {
   // public static ProductMapper INSTANCE = Mappers.getMapper( ProductMapper.class );

    ProductResponse ProductToProductDto(Product product);

  //  @Mapping(target = "totalPrice" ,source = "totalPrice",defaultExpression = "java(productDto.getPrice().add(productDto.getPrice().multiply(BigDecimal.valueOf(Double.valueOf(productDto.getTax())).divide(new BigDecimal(100)))))")
    Product ProductDtoToProduct(ProductRequest productRequest);

}
