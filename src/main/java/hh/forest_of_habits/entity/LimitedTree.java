package hh.forest_of_habits.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

//@Entity
//@DiscriminatorValue("LIMITED_TREE")
public class LimitedTree extends Tree {

    private Integer limit;
}