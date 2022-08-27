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

public class CategoryTest {
    String resource = "mybatis-config.xml";
    SqlSession session = null;

    @Test
    void selectCategory() throws IOException {
        try {

            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();

            db.dao.CategoriesMapper categoriesMapper = session.getMapper(db.dao.CategoriesMapper.class);
            db.model.CategoriesExample categoriesExample = new db.model.CategoriesExample();

            db.model.Categories selected = categoriesMapper.selectByPrimaryKey(2L);
            Assertions.assertEquals(2, selected.getId());
            Assertions.assertEquals("Electronic", selected.getTitle());

            categoriesExample.createCriteria().andIdEqualTo(1L);
            List<db.model.Categories> list = categoriesMapper.selectByExample(categoriesExample);
            Assertions.assertEquals(1, categoriesMapper.countByExample(categoriesExample));
            Assertions.assertEquals("Food", list.get(0).getTitle());
            Assertions.assertEquals(1, list.get(0).getId());

        } finally {
            session.close();
        }
    }
}


