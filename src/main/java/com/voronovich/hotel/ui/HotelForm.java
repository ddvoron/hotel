package com.voronovich.hotel.ui;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import com.voronovich.hotel.pojo.Hotel;
import com.voronovich.hotel.pojo.HotelCategory;
import com.voronovich.hotel.service.HotelService;

public class HotelForm extends FormLayout{

	private TextField name = new TextField("Name");
	private TextField address = new TextField("Address");
	private TextField rating = new TextField("Rating");
	private DateField operatesFrom = new DateField("Operates From");
	private NativeSelect<HotelCategory> category = new NativeSelect<>("Category");
	private TextField url = new TextField("URL");
	private TextField description = new TextField("Description");
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");
	
	private HotelService service = HotelService.getInstance();
	private Hotel hotel;
	private MyUI myUI;
	private Binder<Hotel> binder = new Binder<>(Hotel.class);
	
	public HotelForm(MyUI myUI){
		this.myUI = myUI;
		
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(save,delete);
		addComponents(name,address,rating, operatesFrom, category, url, description, buttons);
		
		category.setItems(HotelCategory.values());
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);
		

		
		
		binder.forField(rating).withConverter(new StringToIntegerConverter(0,"Only digits"))
		.bind(Hotel::getRating, Hotel::setRating);
		binder.forField(name).bind(Hotel::getName, Hotel::setName);
		binder.forField(address).bind(Hotel::getAddress, Hotel::setAddress);
		binder.forField(url).bind(Hotel::getUrl, Hotel::setUrl);
		binder.forField(category).bind(Hotel::getCategory, Hotel::setCategory);
		binder.forField(description).bind(Hotel::getDescription, Hotel::setDescription);
		binder.forField(operatesFrom).bind(Hotel::getOperatesFrom, Hotel::setOperatesFrom);

		
		
		
		
		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());
	}
	
	public void setHotel(Hotel hotel){
		this.hotel = hotel;
		binder.setBean(hotel);
		
		delete.setVisible(hotel.isPersisted());
		setVisible(true);
		name.selectAll();
	}
	
	private void delete(){
		service.delete(hotel);
		myUI.updateHotels();
		setVisible(false);
	}
	
	private void save(){
		service.save(hotel);
		myUI.updateHotels();
		setVisible(false);
	}
}
