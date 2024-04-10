package hh.forest_of_habits.entity;
import hh.forest_of_habits.enums.TreePeriod;
import jakarta.persistence.*;

//@Entity
//@DiscriminatorValue("PERIODIC_TREE")
public class PeriodicTree extends Tree {

    @Enumerated(EnumType.STRING)
    private TreePeriod period;

}