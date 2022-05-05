package com.disco.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: Brand
 * @Description: 品牌实体类
 * @date: 2022/5/5
 * @author zhb
 */
@Table(name = "tb_brand")
public class Brand {

    public Brand() {
    }

    public Brand(Long id, String name, String image, Character letter) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.letter = letter;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
    // 首字母
    private Character letter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Character getLetter() {
        return letter;
    }

    public void setLetter(Character letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return "Brand{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", image='" + image + '\'' +
            ", letter=" + letter +
            '}';
    }
}
