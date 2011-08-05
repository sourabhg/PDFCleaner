package pdfmt;

import java.io.File;

import javax.swing.filechooser.FileFilter;

// TODO: Auto-generated Javadoc
/**
 * The Class ExtensionFileFilter.
 */
class ExtensionFileFilter extends FileFilter {
	  
  	/** The description. */
  	String description;

	  /** The extensions. */
  	String extensions[];

	  /**
  	 * Instantiates a new extension file filter.
  	 *
  	 * @param description the description
  	 * @param extension the extension
  	 */
  	public ExtensionFileFilter(String description, String extension) {
	    this(description, new String[] { extension });
	  }

	  /**
  	 * Instantiates a new extension file filter.
  	 *
  	 * @param description the description
  	 * @param extensions the extensions
  	 */
  	public ExtensionFileFilter(String description, String extensions[]) {
	    if (description == null) {
	      this.description = extensions[0];
	    } else {
	      this.description = description;
	    }
	    this.extensions = (String[]) extensions.clone();
	    toLower(this.extensions);
	  }

	  /**
  	 * To lower.
  	 *
  	 * @param array the array
  	 */
  	private void toLower(String array[]) {
	    for (int i = 0, n = array.length; i < n; i++) {
	      array[i] = array[i].toLowerCase();
	    }
	  }

	  /* (non-Javadoc)
  	 * @see javax.swing.filechooser.FileFilter#getDescription()
  	 */
  	public String getDescription() {
	    return description;
	  }

	  /* (non-Javadoc)
  	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
  	 */
  	public boolean accept(File file) {
	    if (file.isDirectory()) {
	      return true;
	    } else {
	      String path = file.getAbsolutePath().toLowerCase();
	      for (int i = 0, n = extensions.length; i < n; i++) {
	        String extension = extensions[i];
	        if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
	          return true;
	        }
	      }
	    }
	    return false;
	  }
	}
