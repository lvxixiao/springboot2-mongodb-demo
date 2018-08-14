package lvxixiao.dao.repo;

import lvxixiao.bean.AuthorPo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorRepo extends MongoRepository<AuthorPo,String> {
}
