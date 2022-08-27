package lesson6;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ProductTest {
    String resource = "mybatis-config.xml";
    SqlSession session = null;

    @Test
    void selectProduct() throws IOException {
        try {

            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();

            db.dao.ProductsMapper productsMapper = session.getMapper(db.dao.ProductsMapper.class);
            db.model.ProductsExample productsExample = new db.model.ProductsExample();

            db.model.Products selected = productsMapper.selectByPrimaryKey(1L);
            Assertions.assertEquals(1, selected.getId());
            Assertions.assertEquals("Milk", selected.getTitle());
            Assertions.assertEquals(1, selected.getCategory_id());
            Assertions.assertEquals(95, selected.getPrice());

            productsExample.createCriteria().andIdEqualTo(4L);
            List<db.model.Products> list = productsMapper.selectByExample(productsExample);
            Assertions.assertEquals(1, productsMapper.countByExample(productsExample));
            Assertions.assertEquals("Samsung Watch X1000", list.get(0).getTitle());
            Assertions.assertEquals(2, list.get(0).getCategory_id());
            Assertions.assertEquals(20000,list.get(0).getPrice());

        } finally {
            session.close();
        }
    }








}
