package pl.edu.agh.pierogi.visitor;

public interface Visitable {
    void accept(Visitor visitor);
}
