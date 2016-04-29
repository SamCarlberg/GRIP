package edu.wpi.grip.core.operations.opencv.templated;

import com.google.common.collect.ImmutableList;
import edu.wpi.grip.core.Operation;
import edu.wpi.grip.core.OperationDescription;
import edu.wpi.grip.core.sockets.InputSocket;
import edu.wpi.grip.core.sockets.OutputSocket;
import edu.wpi.grip.core.sockets.SocketHint;

import java.util.List;


public class SixSourceOneDestinationOperation<T1, T2, T3, T4, T5, T6, R> implements Operation {
    private final InputSocket<T1> input1;
    private final InputSocket<T2> input2;
    private final InputSocket<T3> input3;
    private final InputSocket<T4> input4;
    private final InputSocket<T5> input5;
    private final InputSocket<T6> input6;
    private final OutputSocket<R> output;
    private final Performer<T1, T2, T3, T4, T5, T6, R> performer;

    @FunctionalInterface
    public interface Performer<T1, T2, T3, T4, T5, T6, R> {
        void perform(T1 src1, T2 src2, T3 src3, T4 src4, T5 src5, T6 src6, R dst);
    }

    public SixSourceOneDestinationOperation(
            InputSocket.Factory inputSocketFactory,
            OutputSocket.Factory outputSocketFactory,
            Performer<T1, T2, T3, T4, T5, T6, R> performer,
            SocketHint<T1> t1SocketHint,
            SocketHint<T2> t2SocketHint,
            SocketHint<T3> t3SocketHint,
            SocketHint<T4> t4SocketHint,
            SocketHint<T5> t5SocketHint,
            SocketHint<T6> t6SocketHint,
            SocketHint<R> rSocketHint) {
        this.performer = performer;
        this.input1 = inputSocketFactory.create(t1SocketHint);
        this.input2 = inputSocketFactory.create(t2SocketHint);
        this.input3 = inputSocketFactory.create(t3SocketHint);
        this.input4 = inputSocketFactory.create(t4SocketHint);
        this.input5 = inputSocketFactory.create(t5SocketHint);
        this.input6 = inputSocketFactory.create(t6SocketHint);
        this.output = outputSocketFactory.create(rSocketHint);
    }


    public SixSourceOneDestinationOperation(
            InputSocket.Factory inputSocketFactory,
            OutputSocket.Factory outputSocketFactory,
            Performer<T1, T2, T3, T4, T5, T6, R> performer,
            Class<T1> t1, Class<T2> t2, Class<T3> t3, Class<T4> t4, Class<T5> t5, Class<T6> t6, Class<R> r) {
        this(
                inputSocketFactory,
                outputSocketFactory,
                performer,
                new SocketHint.Builder<>(t1).identifier("src1").build(),
                new SocketHint.Builder<>(t2).identifier("src2").build(),
                new SocketHint.Builder<>(t3).identifier("src3").build(),
                new SocketHint.Builder<>(t4).identifier("src4").build(),
                new SocketHint.Builder<>(t5).identifier("src5").build(),
                new SocketHint.Builder<>(t6).identifier("src6").build(),
                new SocketHint.Builder<>(r).identifier("dst").build());
    }

    @Override
    public OperationDescription getDescription() {
        return null;
    }

    @Override
    public List<InputSocket> getInputSockets() {
        return ImmutableList.of(input1, input2, input3, input4, input5, input6);
    }

    @Override
    public List<OutputSocket> getOutputSockets() {
        return ImmutableList.of(output);
    }

    @Override
    public void perform() {
        performer.perform(
                input1.getValue().get(),
                input2.getValue().get(),
                input3.getValue().get(),
                input4.getValue().get(),
                input5.getValue().get(),
                input6.getValue().get(),
                output.getValue().get()
        );
        output.setValue(output.getValue().get());
    }
}
