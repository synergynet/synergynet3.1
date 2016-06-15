package synergynet3.web.appsystem.persistence;

import java.io.File;

import synergynet3.web.commons.storage.ServerStorageManager;
import synergynet3.web.shared.Database;

import com.db4o.ObjectServer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.Db4oIOException;
import com.db4o.messaging.MessageContext;
import com.db4o.messaging.MessageRecipient;

/**
 * The Class DatabaseServer.
 */
public class DatabaseServer implements MessageRecipient
{

	/** The current db. */
	private static String CURRENT_DB = "s3db";

	/** The Constant DB_DIR. */
	private static final String DB_DIR = ServerStorageManager.getStorageDirForAppWithName("database").getAbsolutePath() + "\\";

	/** The database is set. */
	private boolean databaseIsSet = false;

	/** The stop. */
	private boolean stop = false;

	/**
	 * Instantiates a new database server.
	 */
	public DatabaseServer()
	{
	}

	/**
	 * Gets the database directory.
	 *
	 * @return the database directory
	 */
	public static String getDatabaseDirectory()
	{
		return DB_DIR;
	}

	/**
	 * Close.
	 */
	public void close()
	{
		synchronized (this)
		{
			databaseIsSet = false;
			stop = true;
			this.notify();
		}
	}

	/**
	 * Checks if is database is set.
	 *
	 * @return true, if is database is set
	 */
	public boolean isDatabaseIsSet()
	{
		return databaseIsSet;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.db4o.messaging.MessageRecipient#processMessage(com.db4o.messaging
	 * .MessageContext, java.lang.Object)
	 */
	@Override
	public void processMessage(MessageContext con, Object message)
	{
		if (message instanceof String)
		{
			String messageString = (String) message;
			if (messageString.equals("close"))
			{
				close();
			}
		}
	}

	/**
	 * Run server.
	 */
	public void runServer()
	{
		synchronized (this)
		{

			File dbDir = new File(DB_DIR);
			if (!dbDir.isDirectory())
			{
				dbDir.mkdir();
			}

			databaseIsSet = true;

			try
			{
				ServerConfiguration config = Db4oClientServer.newServerConfiguration();
				config.networking().messageRecipient(this);

				ObjectServer db4oServer = Db4oClientServer.openServer(config, DB_DIR + CURRENT_DB, Database.DB_PORT);
				db4oServer.grantAccess(Database.DB_USER, Database.DB_PASS);

				Thread.currentThread().setName(this.getClass().getName());

				Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
				try
				{
					if (!stop)
					{
						this.wait(Long.MAX_VALUE);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				db4oServer.close();
			}
			catch (Db4oIOException f)
			{
				System.out.println("Database is already running");
			}
			catch (DatabaseFileLockedException e)
			{
				System.out.println("Database is already running");
			}
		}
	}

}
