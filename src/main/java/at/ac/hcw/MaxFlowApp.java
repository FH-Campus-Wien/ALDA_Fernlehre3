package at.ac.hcw;

import at.ac.hcw.maxflow.Edge;
import at.ac.hcw.maxflow.FlowNetwork;
import at.ac.hcw.maxflow.FordFulkerson;
import at.ac.hcw.maxflow.MaxFlowResult;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MaxFlowApp extends Application {
    private static final double NODE_RADIUS = 34;
    private static final double ARROW_LENGTH = 14;
    private static final double ARROW_WIDTH = 8;
    private static final double GRAPH_WIDTH = 900;
    private static final double GRAPH_HEIGHT = 520;
    private static final double HORIZONTAL_PADDING = 110;
    private static final double VERTICAL_PADDING = 80;

    private final FlowNetwork<String> network = SampleNetworks.defaultNetwork();
    private final Map<String, NodeView> nodeViews = new LinkedHashMap<>();
    private final Map<Edge<String>, Label> edgeLabels = new LinkedHashMap<>();
    private final Label resultLabel = new Label("Noch nicht berechnet");
    private final Label pathLabel = new Label("Augmentierende Pfade erscheinen nach dem Start hier.");

    @Override
    public void start(Stage stage) {
        Pane graphPane = new Pane();
        graphPane.setPrefSize(GRAPH_WIDTH, GRAPH_HEIGHT);
        graphPane.setStyle("-fx-background-color: linear-gradient(to bottom, #f6fbff, #e9f1f7);");

        createGraphView(graphPane);
        updateView(null);

        Label title = new Label("Ford-Fulkerson mit Tiefensuche");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));

        Label subtitle = new Label("Eigene Graph-Implementierung mit Anzeige von Fluss und Kapazitaet");
        subtitle.setTextFill(Color.web("#4a6172"));

        Button runButton = new Button("Maximalen Fluss berechnen");
        runButton.setOnAction(event -> runFordFulkerson());

        Button resetButton = new Button("Zuruecksetzen");
        resetButton.setOnAction(event -> {
            network.resetFlows();
            updateView(null);
        });

        HBox controls = new HBox(12, runButton, resetButton, resultLabel);
        controls.setAlignment(Pos.CENTER_LEFT);

        pathLabel.setWrapText(true);
        pathLabel.setMaxWidth(860);

        VBox header = new VBox(10, title, subtitle, controls, pathLabel);
        header.setPadding(new Insets(18, 20, 12, 20));

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(graphPane);

        Scene scene = new Scene(root, 940, 680);
        stage.setTitle("Ford-Fulkerson Visualisierung");
        stage.setScene(scene);
        stage.show();
    }

    private void runFordFulkerson() {
        network.resetFlows();
        FordFulkerson<String> algorithm = new FordFulkerson<>();
        MaxFlowResult<String> result = algorithm.computeMaxFlow(network, SampleNetworks.SOURCE, SampleNetworks.SINK);
        updateView(result);
    }

    private void updateView(MaxFlowResult<String> result) {
        for (Edge<String> edge : network.edges()) {
            Label label = edgeLabels.get(edge);
            label.setText(edge.flow() + "/" + edge.capacity());
            label.setTextFill(edge.flow() > 0 ? Color.web("#0b6e4f") : Color.web("#425466"));
        }

        for (Map.Entry<String, NodeView> entry : nodeViews.entrySet()) {
            String vertex = entry.getKey();
            NodeView view = entry.getValue();
            if (SampleNetworks.SOURCE.equals(vertex)) {
                view.annotation.setText("Quelle");
                view.circle.setFill(Color.web("#d8f3dc"));
                view.circle.setStroke(Color.web("#2d6a4f"));
            } else if (SampleNetworks.SINK.equals(vertex)) {
                view.annotation.setText("Senke");
                view.circle.setFill(Color.web("#fde2e4"));
                view.circle.setStroke(Color.web("#bc4749"));
            } else {
                view.annotation.setText("Zwischenknoten");
                view.circle.setFill(Color.web("#ffffff"));
                view.circle.setStroke(Color.web("#3d5a80"));
            }
        }

        if (result == null) {
            resultLabel.setText("Status: bereit");
            pathLabel.setText("Augmentierende Pfade erscheinen nach dem Start hier.");
            return;
        }

        resultLabel.setText("Maximaler Fluss: " + result.maxFlow());
        pathLabel.setText(String.join("   |   ", result.augmentingPaths()));
    }

    private void createGraphView(Pane graphPane) {
        Map<String, Position> positions = calculatePositions();

        for (Edge<String> edge : network.edges()) {
            Position from = positions.get(edge.from());
            Position to = positions.get(edge.to());
            DirectedEdgeGeometry geometry = createDirectedEdgeGeometry(from, to);

            Line line = new Line(geometry.startX(), geometry.startY(), geometry.endX(), geometry.endY());
            line.setStroke(Color.web("#7b8ea3"));
            line.setStrokeWidth(3);
            Polygon arrowHead = new Polygon(
                    geometry.endX(), geometry.endY(),
                    geometry.leftX(), geometry.leftY(),
                    geometry.rightX(), geometry.rightY()
            );
            arrowHead.setFill(Color.web("#7b8ea3"));
            graphPane.getChildren().addAll(line, arrowHead);

            Label label = new Label();
            label.setStyle("-fx-background-color: rgba(255,255,255,0.92); -fx-padding: 4 8 4 8; -fx-background-radius: 10;");
            label.setLayoutX((geometry.startX() + geometry.endX()) / 2 - 24);
            label.setLayoutY((geometry.startY() + geometry.endY()) / 2 - 12);
            edgeLabels.put(edge, label);
            graphPane.getChildren().add(label);
        }

        for (Map.Entry<String, Position> entry : positions.entrySet()) {
            String name = entry.getKey();
            Position position = entry.getValue();

            Circle circle = new Circle(position.x(), position.y(), NODE_RADIUS);
            circle.setStrokeWidth(3);

            Label nameLabel = new Label(name);
            nameLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
            nameLabel.setMinWidth(34);
            nameLabel.setAlignment(Pos.CENTER);
            nameLabel.setLayoutX(position.x() - 17);
            nameLabel.setLayoutY(position.y() - 17);

            Label annotation = new Label();
            annotation.setLayoutX(position.x() - 46);
            annotation.setLayoutY(position.y() + 42);
            annotation.setMinWidth(92);
            annotation.setAlignment(Pos.CENTER);
            annotation.setTextFill(Color.web("#3d5a80"));

            nodeViews.put(name, new NodeView(circle, annotation));
            graphPane.getChildren().addAll(circle, nameLabel, annotation);
        }
    }

    private Map<String, Position> calculatePositions() {
        List<String> vertices = network.vertices().stream()
                .sorted()
                .toList();
        Map<String, Integer> levels = determineLevels();

        int sinkColumn = Math.max(2, levels.values().stream().mapToInt(Integer::intValue).max().orElse(1));
        Map<Integer, List<String>> verticesByColumn = new LinkedHashMap<>();

        for (String vertex : vertices) {
            int column = determineColumn(vertex, levels, sinkColumn);
            verticesByColumn.computeIfAbsent(column, ignored -> new ArrayList<>()).add(vertex);
        }

        Map<String, Position> positions = new LinkedHashMap<>();
        for (Map.Entry<Integer, List<String>> entry : verticesByColumn.entrySet()) {
            int column = entry.getKey();
            List<String> columnVertices = entry.getValue().stream()
                    .sorted()
                    .toList();

            if (column == 0 || column == sinkColumn) {
                String vertex = columnVertices.getFirst();
                positions.put(vertex, new Position(columnToX(column, sinkColumn), GRAPH_HEIGHT / 2));
                continue;
            }

            double yStep = (GRAPH_HEIGHT - 2 * VERTICAL_PADDING) / (columnVertices.size() + 1);
            for (int index = 0; index < columnVertices.size(); index++) {
                String vertex = columnVertices.get(index);
                double y = VERTICAL_PADDING + yStep * (index + 1);
                positions.put(vertex, new Position(columnToX(column, sinkColumn), y));
            }
        }

        return positions;
    }

    private Map<String, Integer> determineLevels() {
        Map<String, Integer> levels = new HashMap<>();
        Queue<String> queue = new ArrayDeque<>();
        levels.put(SampleNetworks.SOURCE, 0);
        queue.add(SampleNetworks.SOURCE);

        while (!queue.isEmpty()) {
            String current = queue.remove();
            int nextLevel = levels.get(current) + 1;

            for (Edge<String> edge : network.outgoingEdgesOf(current)) {
                if (edge.isResidual()) {
                    continue;
                }

                String neighbor = edge.to();
                if (levels.containsKey(neighbor)) {
                    continue;
                }

                levels.put(neighbor, nextLevel);
                queue.add(neighbor);
            }
        }

        Set<String> missingVertices = new LinkedHashSet<>(network.vertices());
        missingVertices.removeAll(levels.keySet());
        for (String vertex : missingVertices.stream().sorted().toList()) {
            levels.put(vertex, 1);
        }

        levels.put(SampleNetworks.SINK, Math.max(levels.getOrDefault(SampleNetworks.SINK, 1), 2));
        return levels;
    }

    private int determineColumn(String vertex, Map<String, Integer> levels, int sinkColumn) {
        if (SampleNetworks.SOURCE.equals(vertex)) {
            return 0;
        }
        if (SampleNetworks.SINK.equals(vertex)) {
            return sinkColumn;
        }
        return Math.min(Math.max(levels.getOrDefault(vertex, 1), 1), sinkColumn - 1);
    }

    private double columnToX(int column, int sinkColumn) {
        double leftX = HORIZONTAL_PADDING;
        double rightX = GRAPH_WIDTH - HORIZONTAL_PADDING;
        double fraction = column / (double) sinkColumn;
        return leftX + (rightX - leftX) * fraction;
    }

    private DirectedEdgeGeometry createDirectedEdgeGeometry(Position from, Position to) {
        double dx = to.x() - from.x();
        double dy = to.y() - from.y();
        double length = Math.hypot(dx, dy);
        double unitX = dx / length;
        double unitY = dy / length;

        double startX = from.x() + unitX * NODE_RADIUS;
        double startY = from.y() + unitY * NODE_RADIUS;
        double endX = to.x() - unitX * (NODE_RADIUS + 2);
        double endY = to.y() - unitY * (NODE_RADIUS + 2);

        double perpendicularX = -unitY;
        double perpendicularY = unitX;
        double baseX = endX - unitX * ARROW_LENGTH;
        double baseY = endY - unitY * ARROW_LENGTH;

        double leftX = baseX + perpendicularX * ARROW_WIDTH;
        double leftY = baseY + perpendicularY * ARROW_WIDTH;
        double rightX = baseX - perpendicularX * ARROW_WIDTH;
        double rightY = baseY - perpendicularY * ARROW_WIDTH;

        return new DirectedEdgeGeometry(startX, startY, endX, endY, leftX, leftY, rightX, rightY);
    }

    private record Position(double x, double y) {
    }

    private record DirectedEdgeGeometry(
            double startX,
            double startY,
            double endX,
            double endY,
            double leftX,
            double leftY,
            double rightX,
            double rightY
    ) {
    }

    private record NodeView(Circle circle, Label annotation) {
    }
}
