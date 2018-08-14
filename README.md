# springboot2-mongodb demo

## description

lvxixiao.dao.DocumentRepo接口实现了MongoRepository接口，提供了简单的增删改查功能，而且spring data mongodb还可以通过解析查询函数名字自动查询。

org.springframework.data.mongodb.core.MongoOperations接口定义了一系列便利的方法用来操作MongoDB，使用时通过Spring容器获得其实现类。

**模糊查询**

需要借助MongoDB中正则表达式的功能，并且MongoDB还提供了四种regex操作符。

 * i
    * 忽略大小写
 * m
    * 多行匹配模式，该选项会更改^和$元字符的默认行为，分别使用与行的开头和结尾匹配，而不是与输入字符串的开头和结尾匹配。
 * x
    * 忽略非转义的空白字符。设置该选项后，正则表达式中的非转义的空白字符将被忽略，同时井号(#)被解释为注释的开头注，只能显式位于option选项中。
 * s
    * 单行匹配模式。设置该选项后，会改变模式中的点号(.)元字符的默认行为，它会匹配所有字符，包括换行符(\n)，只能显式位于option选项中。

```
@Autowired
private MongoOperations mongoOps;

public List<DocumentPo> findByContent(String content){
    return mongoOps.find(query(where("content").regex(content,"im")),DocumentPo.class);
}
```

**联表查询**

借助聚合查找中的lookup功能。

需要注意的时，此处_id字段查询结果为null。目前尚未找到原因，但是换成author集合中的其他字段，则该查询没有问题。
```
private MongoOperations mongoOps;

public AggregationResults findById(String id){
    TypedAggregation<DocumentPo> agg = newAggregation(
        DocumentPo.class,
        match(where("_id").is(id)),
        lookup("author","idAuthor","_id","author")
    );
    AggregationResults<DocumentDto> results = mongoOps.aggregate(agg,DocumentPo.class,DocumentDto.class);
    return results;
}
```

**分页查询**

分页查询借助org.springframework.data.domain.Pageable类实现。

需要注意的时，页从第0页开始。

```
private MongoOperations mongoOps;

public List<DocumentPo> findByIdAuthor(String idAuthor,int page,int size) {
    Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC,"title"));
    Pageable pageable = PageRequest.of(page, size,sort);
    return mongoOps.find(query(where("idAuthor").is(idAuthor)).with(pageable),DocumentPo.class);
}
```


## document
> spring data mongodb: https://docs.spring.io/spring-data/mongodb/docs/2.0.5.RELEASE/reference/html/

> springboot 测试：https://docs.spring.io/spring-boot/docs/2.0.0.RELEASE/reference/htmlsingle/#boot-features-testing

