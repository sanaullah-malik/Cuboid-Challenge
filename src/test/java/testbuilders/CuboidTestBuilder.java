package testbuilders;

import co.fullstacklabs.cuboid.challenge.model.Bag;
import co.fullstacklabs.cuboid.challenge.model.Cuboid;
import lombok.Builder;

public class CuboidTestBuilder {

    @Builder
    public static Cuboid cuboid(final Long id, final float width, final float height,
                                final float depth, Bag bag) {
        final Cuboid cuboid = new Cuboid();
        cuboid.setId(id);
        cuboid.setWidth(width);
        cuboid.setHeight(height);
        cuboid.setDepth(depth);
        cuboid.setBag(bag);
        cuboid.setVolume(Double.valueOf(String.valueOf(height *  width * depth)));
        return cuboid;
    }

    public static class CuboidBuilder {
        private float width = 3f;
        private float height = 2f;
        private float depth = 3f;
        private Bag bag = BagTestBuilder.builder().id(1L).build();
    }
}
