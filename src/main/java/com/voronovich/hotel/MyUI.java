package com.voronovich.hotel;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;
import com.voronovich.hotel.pojo.Hotel;
import com.voronovich.hotel.pojo.HotelCategory;
import com.voronovich.hotel.service.HotelCategoryService;
import com.voronovich.hotel.service.HotelService;

/**
 * Class loader
 * 
 * @author Dmitry
 *
 */
@Theme("mytheme")
public class MyUI extends UI {

	private HotelService service = HotelService.getInstance();
	private HotelCategoryService serviceHC = HotelCategoryService.getInstance();
	private Grid<Hotel> gridHotel = new Grid<>(Hotel.class);
	private Grid<HotelCategory> gridHC = new Grid<>(HotelCategory.class);
	private TextField filterFirst = new TextField();
	private TextField filterSecond = new TextField();
	private HotelForm hotelForm = new HotelForm(this);
	private CategoryForm categoryForm = new CategoryForm(this);

	@Override
	protected void init(VaadinRequest vaadinRequest) {

		final VerticalLayout layout = new VerticalLayout();

		MenuBar barmenu = new MenuBar();
		
		//action for button 1
		MenuBar.Command commandShowHotels = new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				Page.getCurrent().reload();
			}
		};
		//action for button 2
		MenuBar.Command commandShowHotelCategories = new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				layout.removeAllComponents();
				showCategories(barmenu, layout);
			}
		};
		//button1
		MenuItem hotels = barmenu.addItem("Hotels", commandShowHotels);
		//button2
		MenuItem hotelCategories = barmenu.addItem("Hotel Categories", commandShowHotelCategories);

		showHotels(barmenu, layout);
	}
	
	/**
	 * Is loaded by default or by Click on commandShowHotels button
	 * 
	 * @param component - barmenu
	 * @param layout    - main vertical layout
	 */
	public void showHotels(Component component, Layout layout) {

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
		filtering.addComponents(filterFirst, clearFilterFirstButton, filterSecond, clearFilterSecondButton);

		filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		Button addHotelButton = new Button("Add hotel...");
		addHotelButton.addClickListener(hotel -> {
			gridHotel.asSingleSelect().clear();
			hotelForm.setHotel(new Hotel());
		});

		HorizontalLayout toolbar = new HorizontalLayout(filtering, addHotelButton);

		gridHotel.setColumns("name", "address", "rating", "operatesFrom", "category", "description");
		gridHotel.addColumn(e -> "<a href='" + e.getUrl() + "' target='_top'>Explore website</a>", new HtmlRenderer());

		HorizontalLayout main = new HorizontalLayout(gridHotel, hotelForm);
		main.setSizeFull();
		gridHotel.setSizeFull();
		main.setExpandRatio(gridHotel, 1);

		layout.addComponents(component, toolbar, main);

		updateHotels();

		setContent(layout);

		hotelForm.setVisible(false);

		gridHotel.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() == null) {
				hotelForm.setVisible(false);
			} else {
				hotelForm.setHotel(event.getValue());
			}
		});
	}

	/**
	 * Is loaded by click on commandShowHotelCategories button
	 * 
	 * @param component - barmenu
	 * @param layout    - main vertical layout
	 */
	public void showCategories(Component component, Layout layout) {
		
		MultiSelectionModel<HotelCategory> selectionModel
	      = (MultiSelectionModel<HotelCategory>) gridHC.setSelectionMode(SelectionMode.MULTI);

		selectionModel.selectAll();
		
		Button addHotelCategoryButton = new Button("Add hotel category...");
		addHotelCategoryButton.addClickListener(hotel -> {
			gridHC.asMultiSelect().clear();
			categoryForm.setCategory(new HotelCategory());
		});

		HorizontalLayout toolbar = new HorizontalLayout(addHotelCategoryButton);

		gridHC.setColumns("category");

		HorizontalLayout main = new HorizontalLayout(gridHC, categoryForm);
		main.setSizeFull();
		gridHC.setSizeFull();
		main.setExpandRatio(gridHC, 1);

		layout.addComponents(component, toolbar, main);

		updateCategories();

		setContent(layout);

		categoryForm.setVisible(false);

		gridHC.asMultiSelect().addValueChangeListener(event -> {
			if (event.getValue() == null) {
				categoryForm.setVisible(false);
			} else {
				categoryForm.setCategoryGroup(event.getValue());
			}
		});
	}

	public void updateHotels() {
		List<Hotel> hotels = service.findAllByNameAndAddress(filterFirst.getValue(), filterSecond.getValue());
		gridHotel.setItems(hotels);
	}
	
	public void updateCategories() {
		List<HotelCategory> hotelCategories = serviceHC.getAll();
		gridHC.setItems(hotelCategories);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {

		private static final long serialVersionUID = -3318558444241017696L;

	}
}
