package edu.wpi.grip.ui;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import edu.wpi.grip.core.Operation;
import edu.wpi.grip.core.OperationDescription;
import edu.wpi.grip.core.Pipeline;
import edu.wpi.grip.core.Step;
import edu.wpi.grip.ui.annotations.ParametrizedController;
import edu.wpi.grip.ui.dragging.OperationDragService;
import edu.wpi.grip.ui.util.StyleClassNameUtility;

import java.util.Collections;
import java.util.function.Supplier;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

/**
 * A JavaFX control that renders information about an {@link Operation}.  This is used in the palette view to present
 * the user with information on the various operations to choose from.
 */
@ParametrizedController(url = "Operation.fxml")
public class OperationController implements Controller {

    @FXML
    private GridPane root;

    @FXML
    private Label name;

    @FXML
    private Label description;

    @FXML
    private ImageView icon;

    private final Pipeline pipeline;
    private final Step.Factory stepFactory;
    private final OperationDescription operationDescription;
    private final Supplier<Operation> operationSupplier;
    private final OperationDragService operationDragService;

    public interface Factory {
        OperationController create(OperationDescription operationDescription, Supplier<Operation> operationSupplier);
    }

    @Inject
    OperationController(Pipeline pipeline,
                        Step.Factory stepFactory,
                        OperationDragService operationDragService,
                        @Assisted OperationDescription operationDescription,
                        @Assisted Supplier<Operation> operationSupplier) {
        this.pipeline = pipeline;
        this.stepFactory = stepFactory;
        this.operationDescription = operationDescription;
        this.operationSupplier = operationSupplier;
        this.operationDragService = operationDragService;
    }

    @FXML
    public void initialize() {
        root.setId(StyleClassNameUtility.idNameFor(this.operationDescription));
        this.name.setText(this.operationDescription.getName());
        this.description.setText(this.operationDescription.getSummary());

        final Tooltip tooltip = new Tooltip(this.operationDescription.getSummary());
        tooltip.setPrefWidth(400.0);
        tooltip.setWrapText(true);
        Tooltip.install(root, tooltip);

        this.description.setAccessibleHelp(this.operationDescription.getSummary());

        this.operationDescription.getIcon().ifPresent(icon -> this.icon.setImage(new Image(icon)));

        // Ensures that when this element is hidden that it also removes its size calculations
        root.managedProperty().bind(root.visibleProperty());
        root.setOnDragDetected(mouseEvent -> {
            // Create a snapshot to use as the cursor
            final ImageView preview = new ImageView(root.snapshot(null, null));

            final Dragboard db = root.startDragAndDrop(TransferMode.ANY);
            db.setContent(Collections.singletonMap(DataFormat.PLAIN_TEXT, operationDescription.getName()));
            db.setDragView(preview.getImage());
            // Begin the dragging
            root.startFullDrag();
            // Tell the drag service that this is the operation that will be received
            operationDragService.beginDrag(operationSupplier.get());

            mouseEvent.consume();
        });
    }

    @FXML
    public void addStep() {
        this.pipeline.addStep(stepFactory.create(this.operationSupplier.get()));
    }

    public GridPane getRoot() {
        return root;
    }

    public OperationDescription getOperationDescription() {
        return operationDescription;
    }
}
