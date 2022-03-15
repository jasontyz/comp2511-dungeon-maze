package unsw.components;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestTransformComponent {

	TransformComponent transform
		= new TransformComponent(new Entity(), 0, 0);

	@Test
	void testTranslate() {
		transform.translate(10, 10);
		assertEquals(
			new TransformComponent(new Entity(), 10, 10),
			transform
		);
	}

	@Test
	void testObserver() {
		transform.addObserver((Delta<Point2D> delta) -> {
			assertEquals(new Point2D(0, 0), delta.from);
			assertEquals(new Point2D(20, 20), delta.to);
		});
		transform.translate(20, 20);
	}
}
