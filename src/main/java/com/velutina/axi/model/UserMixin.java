package com.velutina.axi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

abstract class UserMixin {
    @JsonIgnore
    abstract String getPassword();
}