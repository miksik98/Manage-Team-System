package pl.edu.agh.pierogi.visitor;

import pl.edu.agh.pierogi.model.Person;

public interface Visitor {
    void visit(Person person);
}
