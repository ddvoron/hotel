package com.voronovich.hotel;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.DateTimeRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import com.voronovich.hotel.pojo.Hotel;
import com.voronovich.hotel.pojo.HotelCategory;
import com.voronovich.hotel.service.HotelCategoryService;
import com.voronovich.hotel.service.HotelService;
import com.voronovich.hotel.util.DateConverter;

/**
 * Form layout for Hotels
 * 
 * @author Dmitry
 * @version 1.0
 */
public class HotelForm extends FormLayout {

	private TextField name = new TextField("Name");
	private TextField address = new TextField("Address");
	private TextField rating = new TextField("Rating");
	private DateField operatesFrom = new DateField("Operates From");
	private NativeSelect<String> category = new NativeSelect<>("Category");
	private TextField url = new TextField("URL");
	private TextArea description = new TextArea("Description");
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");

	private HotelService service = HotelService.getInstance();
	private HotelCategoryService serviceHC = HotelCategoryService.getInstance();
	private Hotel hotel;
	private MyUI myUI;
	private Binder<Hotel> binder = new Binder<>(Hotel.class);

	public HotelForm(MyUI myUI) {
		this.myUI = myUI;

		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(save, delete);
		addComponents(name, address, rating, operatesFrom, category, url, description, buttons);

		ArrayList<String> list = new ArrayList<>();
		for (HotelCategory hC : serviceHC.getInstance().getAll()) {
			list.add(hC.getCategory());
		}

		category.setItems(list);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);

		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());

		bindFields();
	}
	
	/**
	 * Binding fields to form
	 * 
	 */
	public void bindFields() {

		// marked fields as required
		name.setRequiredIndicatorVisible(true);
		address.setRequiredIndicatorVisible(true);
		rating.setRequiredIndicatorVisible(true);
		url.setRequiredIndicatorVisible(true);
		category.setRequiredIndicatorVisible(true);
		operatesFrom.setRequiredIndicatorVisible(true);

		// tool tips addition
		name.setDescription("Hotel name");
		address.setDescription("Hotel address");
		rating.setDescription("Hotel stars");
		url.setDescription("Link to webpage");
		category.setDescription("Hotel type");
		description.setDescription("Additional information about hotel");
		operatesFrom.setDescription("Works since...");

		// validation and required flags
		binder.forField(name).asRequired("Shouldn't be empty!").bind(Hotel::getName, Hotel::setName);
		binder.forField(address).asRequired("Shouldn't be empty!").bind(Hotel::getAddress, Hotel::setAddress);
		binder.forField(url).asRequired("Shouldn't be empty!").bind(Hotel::getUrl, Hotel::setUrl);
		binder.forField(category).asRequired("Shouldn't be empty!").bind(Hotel::getCategory, Hotel::setCategory);
		binder.forField(description).bind(Hotel::getDescription, Hotel::setDescription);
		binder.forField(rating).asRequired("Shouldn't be empty!")
				.withConverter(new StringToIntegerConverter(0, "Only digits"))
				.withValidator(v -> v > 0, "Rating should be greater then 0")
				.withValidator(v -> v < 6, "Rating should be less then 6").bind(Hotel::getRating, Hotel::setRating);
		binder.forField(operatesFrom).asRequired("Shouldn't be empty!")
				.withConverter(new DateConverter())
				.bind(Hotel::getOperatesFrom, Hotel::setOperatesFrom);
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
		binder.setBean(hotel);

		delete.setVisible(hotel.isPersisted());
		setVisible(true);
		name.selectAll();
	}

	private void delete() {
		service.delete(hotel);
		myUI.updateHotels();
		setVisible(false);
	}

	private void save() {
		service.save(hotel);
		myUI.updateHotels();
		setVisible(false);
	}
}
