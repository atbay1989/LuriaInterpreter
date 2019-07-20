package visitor;

public interface Visitable {

	<E> E accept(ReturnVisitor<E> visitor);
	<E> void accept(VoidVisitor<E> visitor);
	
}
