package visitor;

public interface Visitable {

	public abstract Object accept(Visitor visitor);

}
