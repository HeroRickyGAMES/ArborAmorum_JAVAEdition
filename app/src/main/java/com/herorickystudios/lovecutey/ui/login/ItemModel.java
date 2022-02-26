package com.herorickystudios.lovecutey.ui.login;

public class ItemModel {

    private int image;

    //depois eu vou mudar isso para o atrubuto nome de usuario recuperado pelo banco de dados.
    private String username,idade,descricao,localizacao;

    public ItemModel(int image, String username, String idade, String descricao, String localizacao) {
        this.image = image;
        this.username = username;
        this.idade = idade;
        this.descricao = descricao;
        this.localizacao = localizacao;
    }

    public int getImage() {
        return image;
    }

    public String getUsername() {
        return username;
    }

    public String getIdade() {
        return idade;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getLocalizacao() {
        return localizacao;
    }
}
