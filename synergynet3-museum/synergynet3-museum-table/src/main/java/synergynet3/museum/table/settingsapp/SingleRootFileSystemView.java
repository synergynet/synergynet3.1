package synergynet3.museum.table.settingsapp;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

/**
 * The Class SingleRootFileSystemView.
 */
public class SingleRootFileSystemView extends FileSystemView
{

	/** The root. */
	File root;

	/** The roots. */
	File[] roots = new File[1];

	/**
	 * Instantiates a new single root file system view.
	 *
	 * @param root
	 *            the root
	 */
	public SingleRootFileSystemView(File root)
	{
		super();
		this.root = root;
		roots[0] = root;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.filechooser.FileSystemView#createNewFolder(java.io.File)
	 */
	@Override
	public File createNewFolder(File containingDir)
	{
		File folder = new File(containingDir, "New Folder");
		folder.mkdir();
		return folder;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.filechooser.FileSystemView#getDefaultDirectory()
	 */
	@Override
	public File getDefaultDirectory()
	{
		return root;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.filechooser.FileSystemView#getHomeDirectory()
	 */
	@Override
	public File getHomeDirectory()
	{
		return root;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.filechooser.FileSystemView#getRoots()
	 */
	@Override
	public File[] getRoots()
	{
		return roots;
	}
}
