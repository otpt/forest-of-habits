package hh.forest_of_habits.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("LIMITED_TREE")
public class LimitedTree extends Tree {

    @Column
    private Integer limit;
}