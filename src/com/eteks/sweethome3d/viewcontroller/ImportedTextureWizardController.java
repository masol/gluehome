/*
 * ImportedTextureWizardController.java 01 oct 2008
 *
 * Sweet Home 3D, Copyright (c) 2008 Emmanuel PUYBARET / eTeks <info@eteks.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.eteks.sweethome3d.viewcontroller;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.spolo.glue.FurnituresInfomation;
import org.spolo.glue.GUtil;
import org.spolo.glue.GlueUtil;
import org.spolo.glue.GutilResult;
import org.spolo.glue.JsonUtil;
import org.spolo.glue.TextureBean;
import org.spolo.glue.TextureInfomation;

import com.eteks.sweethome3d.model.CatalogTexture;
import com.eteks.sweethome3d.model.Content;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.RecorderException;
import com.eteks.sweethome3d.model.TexturesCatalog;
import com.eteks.sweethome3d.model.TexturesCategory;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.ImportedTextureWizardStepsPanel;
import com.eteks.sweethome3d.tools.TemporaryURLContent;

/**
 * Wizard controller for background image in plan.
 * @author Emmanuel Puybaret
 */
public class ImportedTextureWizardController extends WizardController 
                                             implements Controller {
  public enum Property {STEP, IMAGE, NAME, CATEGORY, WIDTH, HEIGHT}

  public enum Step {IMAGE, ATTRIBUTES};
  
  private Logger log = Logger.getLogger(GlueController.class.getName());

  private final CatalogTexture                 texture;
  private final String                         textureName;
  private final UserPreferences                preferences;
  private final ViewFactory                    viewFactory;
  private final ContentManager                 contentManager;
  private final PropertyChangeSupport          propertyChangeSupport;
  private final String 						   flag;

  private  ImportedTextureWizardStepState textureImageStepState;
  private  ImportedTextureWizardStepState textureAttributesStepState;
  private View                                 stepsView;

  private Step              step;
  private Content           image;
  private String            name;
  private TexturesCategory  category;
  private float             width;
  private float             height;
  private String id;

  /**
   * Creates a controller that edits a new catalog texture.
   */
  public ImportedTextureWizardController(String flag,UserPreferences preferences,
                                         ViewFactory    viewFactory,
                                         ContentManager contentManager) {
    this(flag,null, null, preferences, viewFactory, contentManager);    
  }
  
  /**
   * Creates a controller that edits a new catalog texture with a given 
   * <code>textureName</code>.
   */
  public ImportedTextureWizardController(String flag,String textureName,
                                         UserPreferences preferences,
                                         ViewFactory    viewFactory,
                                         ContentManager contentManager) {
    this(flag,null, textureName, preferences, viewFactory, contentManager);    
  }
  
  /**
   * Creates a controller that edits <code>texture</code> values.
   */
  public ImportedTextureWizardController(String flag,CatalogTexture texture,
                                         UserPreferences preferences,
                                         ViewFactory    viewFactory,
                                         ContentManager contentManager) {
    this(flag,texture, null, preferences, viewFactory, contentManager);    
  }
  
  private ImportedTextureWizardController(String flag,CatalogTexture texture,
                                          String textureName,
                                          UserPreferences preferences,
                                          ViewFactory    viewFactory,
                                          ContentManager contentManager) {
    super(preferences, viewFactory);
    this.flag = flag;
    this.texture = texture;
    this.textureName = textureName;
    this.preferences = preferences;
    this.viewFactory = viewFactory;
    this.contentManager = contentManager;
    this.propertyChangeSupport = new PropertyChangeSupport(this);
	if (flag == null) {
		setTitle(this.preferences.getLocalizedString(
				ImportedTextureWizardController.class,
				texture == null ? "importTextureWizard.title"
						: "modifyTextureWizard.title"));
		// Initialize states
		this.textureImageStepState = new TextureImageStepState();
		this.textureAttributesStepState = new TextureAttributesStepState();
		setStepState(this.textureImageStepState);
	}
	if (flag == "import") {
		System.out.println("[Debug]:" + flag);
		downloadCloudTexutre();
	}
  }
  
  
	/*
	 * DownLoad Cloud Textures
	 */
	private void downloadCloudTexutre() {
		Collection<File> f = DownloadCloudTexture();
		if (f != null && !f.isEmpty()) {
			Iterator<File> FCiterator = f.iterator();
			while (FCiterator.hasNext()) {
				File jsonfile = FCiterator.next();
				importCloudTexture(jsonfile);
			}
		} else {
			System.out.println("[ERROR]:There is no Texture in collection");
		}
	}
	
	/*
	 * import cloud texture from json file
	 */
	public void importCloudTexture(File jsonfile) {
		System.out.println(jsonfile.getName());
		String id = jsonfile.getName().toString().replace(".json", "");
		TextureBean json = null;
		json = (TextureBean) JsonUtil.readJson(jsonfile, TextureBean.class);
		String imagepath = null;
		String tempPath = jsonfile.getParent() + File.separator + id + ".jpg";
		File imageFile = new File(tempPath);
		if (imageFile.exists()) {
			imagepath = tempPath;
		}else {
			tempPath = jsonfile.getParent() + File.separator + id + ".png";
			imagepath = tempPath;
		}
		float length = Float.valueOf(json.getLength());
		float width = Float.valueOf(json.getWidth());
		String textureID = id;
		String seat = json.getResourceName();
		upadateTextureList(imagepath, textureID, length, width, seat);
	}
	
	/*
	 * update the Texture list
	 */
	private void upadateTextureList(final String imageName, String Glueid,
			float length, float width, String seat) {
		if (seat == null || seat.equals("")) {
			updateController(imageName, this.contentManager, this.preferences,
					Glueid, length, width, null);
			this.finish();
		} else {
			updateController(imageName, this.contentManager, this.preferences,
					Glueid, length, width, seat);
			this.finish();
		}
	}

  /**
   * Changes background image in model and posts an undoable operation.
   */
	@Override
	public void finish() {
		CatalogTexture newTexture = new CatalogTexture(getId(), getName(),
				getImage(), getWidth(), getHeight(), null, true);
		// Remove the edited texture from catalog
		TexturesCatalog catalog = this.preferences.getTexturesCatalog();
		if (this.texture != null) {
			catalog.delete(this.texture);
		}
		coverageTexture(catalog,newTexture);
		catalog.add(this.category, newTexture);
	}
	
	/*
	 * coverage the repeated texture
	 */
	public void coverageTexture(TexturesCatalog catalog,
			CatalogTexture newTexture) {
		List<TexturesCategory> Categorylist = catalog.getCategories();
		for (TexturesCategory category : Categorylist) {
			for (CatalogTexture nt : category.getTextures()) {
				if (newTexture.getName().equals(nt.getName())) {
					catalog.delete(nt);
					break;
				} else {
					continue;
				}
			}
		}
	}
    
  /**
   * Returns the content manager of this controller.
   */
  public ContentManager getContentManager() {
    return this.contentManager;
  }

  /**
   * Returns the current step state.
   */
  @Override
  protected ImportedTextureWizardStepState getStepState() {
    return (ImportedTextureWizardStepState)super.getStepState();
  }
  
  /**
   * Returns the texture image step state.
   */
  protected ImportedTextureWizardStepState getTextureImageStepState() {
    return this.textureImageStepState;
  }

  /**
   * Returns the texture attributes step state.
   */
  protected ImportedTextureWizardStepState getTextureAttributesStepState() {
    return this.textureAttributesStepState;
  }
 
  /**
   * Returns the unique wizard view used for all steps.
   */
  protected View getStepsView() {
    // Create view lazily only once it's needed
    if (this.stepsView == null) {
      this.stepsView = this.viewFactory.createImportedTextureWizardStepsView(flag,this.texture, this.textureName, 
          this.preferences, this);
    }
    return this.stepsView;
  }

  /**
   * Switch in the wizard view to the given <code>step</code>.
   */
  protected void setStep(Step step) {
    if (step != this.step) {
      Step oldStep = this.step;
      this.step = step;
      this.propertyChangeSupport.firePropertyChange(Property.STEP.name(), oldStep, step);
    }
  }
  
  /**
   * Returns the current step in wizard view.
   */
  public Step getStep() {
    return this.step;
  }

  /**
   * Adds the property change <code>listener</code> in parameter to this home.
   */
  public void addPropertyChangeListener(Property property, PropertyChangeListener listener) {
    this.propertyChangeSupport.addPropertyChangeListener(property.name(), listener);
  }

  /**
   * Removes the property change <code>listener</code> in parameter from this home.
   */
  public void removePropertyChangeListener(Property property, PropertyChangeListener listener) {
    this.propertyChangeSupport.removePropertyChangeListener(property.name(), listener);
  }

  /**
   * Sets the image content of the imported texture.
   */
  public void setImage(Content image) {
    if (image != this.image) {
      Content oldImage = this.image;
      this.image = image;
      this.propertyChangeSupport.firePropertyChange(Property.IMAGE.name(), oldImage, image);
    }
  }
  
  /**
   * Returns the image content of the imported texture.
   */
  public Content getImage() {
    return this.image;
  }

  /**
   * Returns the name of the imported texture.
   */
  public String getName() {
    return this.name;
  }
  
  /**
   * Sets the name of the imported texture.
   */
  public void setName(String name) {
    if (name != this.name) {
      String oldName = this.name;
      this.name = name;
      if (this.propertyChangeSupport != null) {
        this.propertyChangeSupport.firePropertyChange(Property.NAME.name(), oldName, name);
      }
    }
  }

	// set Texture id
	public void setId(String id) {
		this.id = id;
	}

	// get Texture id
	public String getId() {
		return this.id;
	}
  
  /**
   * Returns the category of the imported texture.
   */
  public TexturesCategory getCategory() {
    return this.category;
  }
  
  /**
   * Sets the category of the imported texture.
   */
  public void setCategory(TexturesCategory category) {
    if (category != this.category) {
      TexturesCategory oldCategory = this.category;
      this.category = category;
      this.propertyChangeSupport.firePropertyChange(Property.CATEGORY.name(), oldCategory, category);
    }
  }
  
  /**
   * Returns the width.
   */
  public float getWidth() {
    return this.width;
  }
  
  /**
   * Sets the width of the imported texture.
   */
  public void setWidth(float width) {
    if (width != this.width) {
      float oldWidth = this.width;
      this.width = width;
      this.propertyChangeSupport.firePropertyChange(Property.WIDTH.name(), oldWidth, width);
    }
  }

  /**
   * Returns the height.
   */
  public float getHeight() {
    return this.height;
  }

  /**
   * Sets the size of the imported texture.
   */
  public void setHeight(float height) {
    if (height != this.height) {
      float oldHeight = this.height;
      this.height = height;
      this.propertyChangeSupport.firePropertyChange(Property.HEIGHT.name(), oldHeight, height);
    }
  }

  /**
   * Returns <code>true</code> if texture name is valid.
   */
  public boolean isTextureNameValid() {
    return this.name != null
        && this.name.length() > 0
        && this.category != null;
  }

  /**
   * Step state superclass. All step state share the same step view,
   * that will display a different component depending on their class name. 
   */
  protected abstract class ImportedTextureWizardStepState extends WizardControllerStepState {
    private URL icon = ImportedTextureWizardController.class.getResource("resources/importedTextureWizard.png");
    
    public abstract Step getStep();

    @Override
    public void enter() {
      setStep(getStep());
    }
    
    @Override
    public View getView() {
      return getStepsView();
    }    
    
    @Override
    public URL getIcon() {
      return this.icon;
    }
  }
    
  /**
   * Texture image choice step state (first step).
   */
  private class TextureImageStepState extends ImportedTextureWizardStepState {
    public TextureImageStepState() {
      ImportedTextureWizardController.this.addPropertyChangeListener(Property.IMAGE, 
          new PropertyChangeListener() {
              public void propertyChange(PropertyChangeEvent evt) {
                setNextStepEnabled(getImage() != null);
              }
            });
    }
    
    @Override
    public void enter() {
      super.enter();
      setFirstStep(true);
      setNextStepEnabled(getImage() != null);
    }
    
    @Override
    public Step getStep() {
      return Step.IMAGE;
    }
    
    @Override
    public void goToNextStep() {
      setStepState(getTextureAttributesStepState());
    }
  }

  /**
   * Texture image attributes step state (last step).
   */
  private class TextureAttributesStepState extends ImportedTextureWizardStepState {
    private PropertyChangeListener widthChangeListener;
    private PropertyChangeListener heightChangeListener;
    private PropertyChangeListener nameAndCategoryChangeListener;
    
    public TextureAttributesStepState() {
      this.widthChangeListener = new PropertyChangeListener() {
          public void propertyChange(PropertyChangeEvent ev) {
            ImportedTextureWizardController.this.removePropertyChangeListener(Property.HEIGHT, heightChangeListener);
            float ratio = (Float)ev.getNewValue() / (Float)ev.getOldValue();
            setHeight(getHeight() * ratio);
            ImportedTextureWizardController.this.addPropertyChangeListener(Property.HEIGHT, heightChangeListener);
          }
        };
      this.heightChangeListener = new PropertyChangeListener() {
          public void propertyChange(PropertyChangeEvent ev) {
            ImportedTextureWizardController.this.removePropertyChangeListener(Property.WIDTH, widthChangeListener);
            float ratio = (Float)ev.getNewValue() / (Float)ev.getOldValue();
            setWidth(getWidth() * ratio); 
            ImportedTextureWizardController.this.addPropertyChangeListener(Property.WIDTH, widthChangeListener);
          }
        };
      this.nameAndCategoryChangeListener = new PropertyChangeListener() {
          public void propertyChange(PropertyChangeEvent ev) {
            setNextStepEnabled(isTextureNameValid());
          }
        };
    }
    
    @Override
    public void enter() {
      super.enter();
      setLastStep(true);
      
      ImportedTextureWizardController.this.addPropertyChangeListener(Property.WIDTH, this.widthChangeListener);
      ImportedTextureWizardController.this.addPropertyChangeListener(Property.HEIGHT, this.heightChangeListener);
      ImportedTextureWizardController.this.addPropertyChangeListener(Property.NAME, this.nameAndCategoryChangeListener);
      ImportedTextureWizardController.this.addPropertyChangeListener(Property.CATEGORY, this.nameAndCategoryChangeListener);
      
      // Last step is always valid by default
      setNextStepEnabled(isTextureNameValid());
    }
    
    @Override
    public Step getStep() {
      return Step.ATTRIBUTES;
    }
    
    @Override
    public void goBackToPreviousStep() {
      setStepState(getTextureImageStepState());
    }
    
    @Override
    public void exit() {
      ImportedTextureWizardController.this.removePropertyChangeListener(Property.WIDTH, this.widthChangeListener);
      ImportedTextureWizardController.this.removePropertyChangeListener(Property.HEIGHT, this.heightChangeListener);
      ImportedTextureWizardController.this.removePropertyChangeListener(Property.NAME, this.nameAndCategoryChangeListener);
      ImportedTextureWizardController.this.removePropertyChangeListener(Property.CATEGORY, this.nameAndCategoryChangeListener);
    }
  }
  
  public Collection<File> DownloadCloudTexture(){
	  Collection<File> f = null;
			try {
				GutilResult<Collection<File>, Collection<File>, File[], TextureInfomation> result = GUtil
						.execute("OpenTextureLib");
				f = result.getAdditionResourceFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	return f;
  }
  
	private void updateController(final String imageName,
			final ContentManager contentManager,
			final UserPreferences preferences, String Glueid,
			float length, float width, String seat) {
		// Read image in imageLoader executor
		Content imageContent = null;
		try {
			// Copy image to a temporary content to keep a safe access
			// to it until home is saved
			imageContent = TemporaryURLContent
					.copyToTemporaryURLContent(contentManager
							.getContent(imageName));
		} catch (RecorderException ex) {
			// Error message displayed below
		} catch (IOException ex) {
			// Error message displayed below
		}
		if (imageContent == null) {
			System.out.println("[Debug]:imageContent is null");
			return;
		}

		BufferedImage image = null;
		try {
			image = readImage(preferences, imageContent);
		} catch (IOException ex) {
			// image is null
		}

		final BufferedImage readImage = image;
		final Content readContent = imageContent;
		// Update components in dispatch thread
		if (readImage != null) {
			setImage(readContent);
			// Initialize attributes with default values
			setName(contentManager.getPresentationName(imageName,
					ContentManager.ContentType.IMAGE));
			
			if (Glueid != null) {
				setId(Glueid);
			}
			// Use user category as default category and create
			// it if it doesn't exist
			TexturesCategory userCategory = null;
			if(seat == null){
			userCategory = new TexturesCategory(
					preferences.getLocalizedString(
							ImportedTextureWizardStepsPanel.class,
							"userCategory"));
			}else{
				userCategory = new TexturesCategory(seat);
			}
			for (TexturesCategory category : preferences.getTexturesCatalog()
					.getCategories()) {
				if (category.equals(userCategory)) {
					userCategory = category;
					break;
				}
			}
			setCategory(userCategory);
			if (width <= 0.0 || length <= 0.0) {
				width = image.getWidth();
				length = image.getHeight();
				log.log(Level.INFO,"贴图的长、宽不能小于等于0！");
			}
			setWidth(width);
			setHeight(length);
		}
	}

	private BufferedImage readImage(UserPreferences preferences,
			Content imageContent) throws IOException {
		try {
			// Display a waiting image while loading

			// Read the image content
			InputStream contentStream = imageContent.openStream();
			BufferedImage image = ImageIO.read(contentStream);
			contentStream.close();
			if (image != null) {
				return image;
			} else {
				throw new IOException();
			}
		} catch (IOException ex) {
			throw ex;
		}
	}
}
