package swinkar;

import java.awt.EventQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class SwinkarUtil
{
  public static void invokeAndWait( final Runnable runnable )
  {
    invokeAndWait( new Callable<Object>()
    {
      public Object call()
      {
        runnable.run();
        return null;
      }
    } );
  }

  public static <V> V invokeAndWait( final Callable<V> function )
  {
    if( EventQueue.isDispatchThread() )
    {
      throw new IllegalStateException( "Should not be calling invokeAndWait from EDT" );
    }
    try
    {
      final FutureTask<V> task = new FutureTask<V>( function );
      EventQueue.invokeLater( task );
      return task.get();
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }
}
