package com.voronovich.hotel.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;
import com.voronovich.hotel.pojo.Hotel;
import com.voronovich.hotel.pojo.HotelCategory;
import com.voronovich.hotel.service.HotelService;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
	
	private HotelService service = HotelService.getInstance();
	private Grid<Hotel> grid = new Grid<>(Hotel.class);
	private TextField filterFirst = new TextField();
	private TextField filterSecond = new TextField();
	private HotelForm hotelForm = new HotelForm(this);
	
	@Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        filterFirst.setPlaceholder("filtering by name...");
        filterFirst.addValueChangeListener(e -> updateHotels());
        filterFirst.setValueChangeMode(ValueChangeMode.LAZY);
        
        filterSecond.setPlaceholder("filtering by address...");
        filterSecond.addValueChangeListener(e -> updateHotels());
        filterSecond.setValueChangeMode(ValueChangeMode.LAZY);
        
        Button clearFilterFirstButton = new Button(VaadinIcons.CLOSE);
        clearFilterFirstButton.setDescription("Flush filter...");
        clearFilterFirstButton.addClickListener(e -> filterFirst.clear());
        
        Button clearFilterSecondButton = new Button(VaadinIcons.CLOSE);
        clearFilterSecondButton.setDescription("Flush filter...");
        clearFilterSecondButton.addClickListener(e -> filterSecond.clear());
        
        CssLayout filtering = new CssLayout();
        filtering.addComponents(filterFirst, clearFilterFirstButton,filterSecond, clearFilterSecondButton);
    
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        
        Button addHotelButton = new Button("Add hotel...");
        addHotelButton.addClickListener(e -> {
        	grid.asSingleSelect().clear();
        	hotelForm.setHotel(new Hotel());
        });
        
        HorizontalLayout toolbar = new HorizontalLayout(filtering, addHotelButton);
        
        
        grid.setColumns("name","address","rating","operatesFrom","category","description");
        
        grid.addColumn(e ->"<a href='" + e.getUrl() + "' target='_top'>Explore website</a>",
        new HtmlRenderer());
        
        
        HorizontalLayout main = new HorizontalLayout(grid,hotelForm);
        main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid, 1);
        
        layout.addComponents(toolbar, main);
        
        updateHotels();
        
        setContent(layout);
        
        hotelForm.setVisible(false);
        
        grid.asSingleSelect().addValueChangeListener(event -> {
        	if(event.getValue()==null){
        		hotelForm.setVisible(false);
        	} else {
        		hotelForm.setHotel(event.getValue());
        	}
        });
    }

	public void updateHotels(){
		List<Hotel> hotels = service.findAllByNameAndAddress(filterFirst.getValue(), filterSecond.getValue());
	
        grid.setItems(hotels);
	}
	
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

    }
}
