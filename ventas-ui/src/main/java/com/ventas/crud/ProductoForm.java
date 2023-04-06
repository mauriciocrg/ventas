package com.ventas.crud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.ventas.backend.data.Availability;
import com.ventas.backend.data.Producto;
import com.ventas.backend.manageData.ManageCategoria;
import com.ventas.ventas.samples.AttributeExtension;

public class ProductoForm extends ProductoFormDesign {

	private ProductoCrudLogic viewLogic;
	private Binder<Producto> binder;
    private Producto currentProducto;
    
    private static String baseDirectory = new File("").getAbsolutePath();
	
	private String imageFilesPath = baseDirectory+File.separator+"images"+File.separator;
    
	private ManageCategoria manageCategoria = new ManageCategoria();
	
    private static class StockPriceConverter extends StringToIntegerConverter {

        public StockPriceConverter() {
            super("No se puede convertir el valor a " + Integer.class.getName());
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            // do not use a thousands separator, as HTML5 input type
            // number expects a fixed wire/DOM number format regardless
            // of how the browser presents it to the user (which could
            // depend on the browser locale)
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(0);
            format.setDecimalSeparatorAlwaysShown(false);
            format.setParseIntegerOnly(true);
            format.setGroupingUsed(false);
            return format;
        }

        @Override
        public Result<Integer> convertToModel(String value,
                ValueContext context) {
            Result<Integer> result = super.convertToModel(value, context);
            return result.map(stock -> stock == null ? 0 : stock);
        }

    }
    
    class ImageUploader implements Receiver, SucceededListener {
        public File file;

        public OutputStream receiveUpload(String filename,
                                          String mimeType) {
            // Create upload stream
            FileOutputStream fos = null; // Stream to write to
            try {
                // Open the file for writing.
                file = new File(imageFilesPath + filename.substring(0, filename.indexOf("."))+"_"+new Date().getTime()+filename.substring(filename.indexOf("."), filename.length()));
                fos = new FileOutputStream(file);
            } catch (final java.io.FileNotFoundException e) {
                
                return null;
            }
            return fos; // Return the output stream to write to
        }

        public void uploadSucceeded(SucceededEvent event) {}
    };
    
    ImageUploader receiver = new ImageUploader();
    
    public ProductoForm(ProductoCrudLogic crudLogic) {
    	super();
        addStyleName("product-form");
        viewLogic = crudLogic;
        
        categorias.setItems(manageCategoria.listCategoria());
        
        AttributeExtension stockFieldExtension = new AttributeExtension();
        stockFieldExtension.extend(stockProducto);
        stockFieldExtension.setAttribute("type", "number");
        
        Converter<Boolean, Integer> converter = new Converter<Boolean, Integer>() {
			@Override
			public Result<Integer> convertToModel(Boolean value, ValueContext context) {
				// TODO Auto-generated method stub
				Result<Integer> result =  Result.ok(value ? 1 : 0);
				
				return result;
			}

			@Override
			public Boolean convertToPresentation(Integer value, ValueContext context) {
				// TODO Auto-generated method stub
				return value != 0;
			}
        };
        
        uploadFoto.setReceiver(receiver);
        uploadFoto.addSucceededListener(new SucceededListener() {
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				// TODO Auto-generated method stub
				final Image image = new Image(null, new StreamResource(new StreamSource() {
		    		@Override
		    		public InputStream getStream() {
		    			try {
							return  new FileInputStream(receiver.file.getAbsoluteFile());
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
		    		}	
				},receiver.file.getAbsolutePath()));
		    	
		    	image.setWidth(100, Sizeable.Unit.PERCENTAGE);
				image.setHeight(100, Sizeable.Unit.PERCENTAGE);
				
				VerticalLayout imgLayout = new VerticalLayout();
				imgLayout.setMargin(true);
				imgLayout.setSpacing(false);
				imgLayout.setWidth(100,Sizeable.Unit.PERCENTAGE);
				imgLayout.setHeight(100,Sizeable.Unit.PERCENTAGE);
				imgLayout.removeAllComponents();
				imgLayout.addComponent(image);
				imgLayout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
		    	
				fotoPanel.setContent(imgLayout);
				nombreFoto.setReadOnly(false);
		    	nombreFoto.setValue(receiver.file.getName());
		    	nombreFoto.setReadOnly(true);
			}
		});
        
        fechaCompra.setValue(LocalDate.now());
        fechaVensimiento.setValue(LocalDate.now());
        
        fechaCompra.setDateFormat("dd/MM/yyyy");
        fechaCompra.setPlaceholder("dd/MM/yyyy");
        fechaVensimiento.setDateFormat("dd/MM/yyyy");
        fechaVensimiento.setPlaceholder("dd/MM/yyyy");
        nombreFoto.setReadOnly(true);
        
        binder = new BeanValidationBinder<>(Producto.class);
        
        nombreProducto.setRequiredIndicatorVisible(true);
        
        binder.forField(nombreProducto).bind("nombre");
        binder.forField(costoCompra).withConverter(new DolarConverter()).bind("costo_compra");
        binder.forField(costoVenta).withConverter(new DolarConverter()).bind("costo_venta");
        binder.forField(stockProducto).withConverter(new StockPriceConverter()).bind("stock");
        binder.forField(fechaCompra).withConverter(new LocalDateToDateConverter()).bind("fecha_compra");
        binder.forField(fechaVensimiento).withConverter(new LocalDateToDateConverter()).bind("fecha_vencimiento");
        binder.forField(descripcionProducto).bind("descripcion");
        binder.forField(nombreFoto).bind("foto");
        binder.forField(categorias).bind("categoria");
        binder.forField(disponible).withConverter(converter).bind("disponible");

        if(currentProducto != null && currentProducto.getFoto() != null && !currentProducto.getFoto().equals("")) {
	        File imgFile = new File(imageFilesPath + currentProducto.getFoto());
			
			if(imgFile.exists()) {
				Image image = new Image(null,new FileResource(imgFile));
				image.setWidth(100, Sizeable.Unit.PERCENTAGE);
				image.setHeight(100, Sizeable.Unit.PERCENTAGE);
				
				VerticalLayout imgLayout = new VerticalLayout();
				imgLayout.setMargin(true);
				imgLayout.setSpacing(false);
				imgLayout.setWidth(100,Sizeable.Unit.PERCENTAGE);
				imgLayout.setHeight(100,Sizeable.Unit.PERCENTAGE);
				imgLayout.removeAllComponents();
				imgLayout.addComponent(image);
				imgLayout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
		    	
				fotoPanel.setContent(imgLayout);
		    	nombreFoto.setValue(imgFile.getName());
			} else {
				fotoPanel.setContent(null);
			}
        } else {
        	fotoPanel.setContent(null);
        }
		
        binder.bindInstanceFields(this);
         
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            uploadFoto.setEnabled(true);
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });
        
        save.addClickListener(event -> {
            if (currentProducto != null
                    && binder.writeBeanIfValid(currentProducto)) {
                viewLogic.saveProducto(currentProducto);
            }
        });

        discard.addClickListener(
                event -> viewLogic.editProducto(currentProducto));

        cancel.addClickListener(event -> viewLogic.cancelProducto());

        delete.addClickListener(event -> {
            if (currentProducto != null) {
                viewLogic.deleteProducto(currentProducto);
            }
        });
    }

    public void editProducto(Producto producto) {
        if (producto == null) {
        	fotoPanel.setContent(null);
            producto = new Producto();
        }
        currentProducto = producto;
        binder.readBean(producto);

        if(currentProducto.getFoto() != null && !currentProducto.getFoto().equals("")) {
	        File imgFile = new File(imageFilesPath + currentProducto.getFoto());
			
			if(imgFile.exists()) {
				Image image = new Image(null,new FileResource(imgFile));
				image.setWidth(100, Sizeable.Unit.PERCENTAGE);
				image.setHeight(100, Sizeable.Unit.PERCENTAGE);
				
				VerticalLayout imgLayout = new VerticalLayout();
				imgLayout.setMargin(true);
				imgLayout.setSpacing(false);
				imgLayout.setWidth(100,Sizeable.Unit.PERCENTAGE);
				imgLayout.setHeight(100,Sizeable.Unit.PERCENTAGE);
				imgLayout.removeAllComponents();
				imgLayout.addComponent(image);
				imgLayout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
		    	
				fotoPanel.setContent(imgLayout);
				nombreFoto.setReadOnly(false);
		    	nombreFoto.setValue(currentProducto.getFoto());
		    	nombreFoto.setReadOnly(true);
			} else {
				fotoPanel.setContent(null);
			}
        } else {
        	fotoPanel.setContent(null);
        }
        
        // Scroll to the top
        // As this is not a Panel, using JavaScript
        String scrollScript = "window.document.getElementById('" + getId()
                + "').scrollTop = 0;";
        Page.getCurrent().getJavaScript().execute(scrollScript);
    }
}
