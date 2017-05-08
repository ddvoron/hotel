package com.voronovich.hotel;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

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
import com.voronovich.hotel.service.HotelCategoryService;
import com.voronovich.hotel.service.HotelService;

/**
 * Form layout for Hotel Categories
 * 
 * @author Dmitry V
 * @version 1.0
 */
public class CategoryForm extends FormLayout{

	private TextField category = new TextField("Category");
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");
	
	private HotelCategoryService service = HotelCategoryService.getInstance();
	private HotelCategory hotelCategory;
	private Set<HotelCategory> hotelCategories;
	private MyUI myUI;
	private Binder<HotelCategory> binder = new Binder<>(HotelCategory.class);
	
	public CategoryForm(MyUI myUI){
		this.myUI = myUI;
		
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(save,delete);
		addComponents(category, buttons);
		
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
	public void bindFields(){
		category.setDescription("Write new category");
		binder.forField(category).bind(HotelCategory::getCategory, HotelCategory::setCategory);

	}
	
	public void setCategory(HotelCategory hotelCategory){
		this.hotelCategory = hotelCategory;
		binder.setBean(hotelCategory);
		
		delete.setVisible(true);
		setVisible(true);
	}
	
	public void setCategoryGroup(Set<HotelCategory> hotelCategory){
		this.hotelCategories = hotelCategory;
		
		delete.setVisible(true);
		setVisible(true);
	}
	
	private void delete(){
		service.deleteGroup(hotelCategories);
		myUI.updateCategories();
		setVisible(false);
	}
	
	private void save(){
		service.save(hotelCategory);
		myUI.updateCategories();
		setVisible(false);
	}
}
