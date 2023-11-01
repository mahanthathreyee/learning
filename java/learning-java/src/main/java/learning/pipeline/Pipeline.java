package learning.pipeline;

import java.util.function.Function;

public class Pipeline<I, O> {

    Function<I, O> handler;

    public Pipeline(Function<I, O> step) {
        this.handler = step;
    }

    public <K> Pipeline<I, K> next(Function<O, K> step) {
        return new Pipeline<>(
            (I2) -> step.apply(this.handler.apply((I2)))
        );
    }

    public O run(I input) {
        return this.handler.apply(input);
    }
}
