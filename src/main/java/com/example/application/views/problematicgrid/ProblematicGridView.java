package com.example.application.views.problematicgrid;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.application.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "hello", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Problematic Grid")
public class ProblematicGridView extends VerticalLayout {

    private HorizontalLayout topRow;
    private HorizontalLayout buttonRow;


    private final Grid<String> _leftGrid;
    private final Grid<String> _rightGrid;

    private final ListDataProvider<String> _leftGridDataProvider = DataProvider.ofCollection(new ArrayList<>());
    private final ListDataProvider<String> _rightGridDataProvider = DataProvider.ofCollection(new ArrayList<>());


    public ProblematicGridView() {
        add(new Text("Content placeholder"));
        _leftGrid = new Grid<>();
        _rightGrid = new Grid<>();

        _leftGrid.addColumn(text -> text);
        _rightGrid.addColumn(text -> text);
        
        _leftGrid.setWidth("100px");
        _leftGrid.setHeight("100px");
        _rightGrid.setWidth("100px");
        _rightGrid.setHeight("100px");
        
        initItems();

        _leftGrid.setItems(_leftGridDataProvider);
        _rightGrid.setItems(_rightGridDataProvider);

        Button moveRightButton = new Button("Move Right");
        Button moveLeftButton = new Button("Move Left");
        moveRightButton.addClickListener(click -> {
            moveFromLeftToRightGrid(_leftGridDataProvider.getItems());
            selectFirstItem(_rightGrid);
            // Workaround
            //getElement().executeJs("").then(ce -> selectFirstItem(_rightGrid));
        });

        moveLeftButton.addClickListener(click -> {
            moveFromRightToLeftGrid(_rightGridDataProvider.getItems());
            selectFirstItem(_leftGrid);
        });


        topRow = new HorizontalLayout();
        buttonRow = new HorizontalLayout();
        topRow.add(_leftGrid, _rightGrid);
        buttonRow.add(moveRightButton, moveLeftButton);
        this.add(topRow, buttonRow);
    }

    private void moveFromLeftToRightGrid(Collection<String> items) {
        ArrayList<String> itemList = new ArrayList<>(items);
        _leftGridDataProvider.getItems().removeAll(itemList);
        _rightGridDataProvider.getItems().addAll(itemList);
        refreshGrids();
    }

    private void moveFromRightToLeftGrid(Collection<String> items) {
        ArrayList<String> itemList = new ArrayList<>(items);
        _rightGridDataProvider.getItems().removeAll(itemList);
        _leftGridDataProvider.getItems().addAll(itemList);
        refreshGrids();
      }

    private void selectFirstItem(Grid<String> grid) {
        ListDataProvider<String> dataProvider = (ListDataProvider<String>) grid.getDataProvider();
        List<String> items = (List<String>)dataProvider.getItems();
        if(items.isEmpty()) {
          return;
        }
        grid.select(items.get(0));
    }

    private void refreshGrid(Grid<String> grid) {
        grid.getSelectionModel().deselectAll();
        grid.getDataProvider().refreshAll();
    }

    private void refreshGrids() {
        refreshGrid(_leftGrid);
        refreshGrid(_rightGrid);
    }

    void initItems() {
        final List<String> itemsLeft = new ArrayList<>();
        itemsLeft.add("flag");
        itemsLeft.add("test");

        _leftGridDataProvider.getItems().addAll(itemsLeft);
        _rightGridDataProvider.getItems().addAll(new ArrayList<>());
        _leftGridDataProvider.refreshAll();
        _rightGridDataProvider.refreshAll();
    }
}
