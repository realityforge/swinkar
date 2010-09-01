package swinkar;

import java.lang.ref.WeakReference;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

public class CheckThreadViolationRepaintManager
  extends RepaintManager
{
  private WeakReference<JComponent> m_lastComponent;

  private boolean m_fullCheck = true;

  public final boolean isFullCheck()
  {
    return m_fullCheck;
  }

  public final void setFullCheck( final boolean fullCheck )
  {
    m_fullCheck = fullCheck;
  }

  public synchronized void addInvalidComponent( final JComponent component )
  {
    checkThreadViolations( component );
    super.addInvalidComponent( component );
  }

  @Override
  public void addDirtyRegion( final JComponent component,
                              final int x,
                              final int y,
                              final int w,
                              final int h )
  {
    checkThreadViolations( component );
    super.addDirtyRegion( component, x, y, w, h );
  }

  private void checkThreadViolations( final JComponent c )
  {
    if( !SwingUtilities.isEventDispatchThread() && ( isFullCheck() || c.isShowing() ) )
    {
      boolean repaint = false;
      boolean fromSwing = false;
      boolean imageUpdate = false;
      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
      for( StackTraceElement st : stackTrace )
      {
        if( repaint && st.getClassName().startsWith( "javax.swing." ) &&
            // for details see
            // https://swinghelper.dev.java.net/issues/show_bug.cgi?id=1
            !st.getClassName().startsWith( "javax.swing.SwingWorker" ) )
        {
          fromSwing = true;
        }
        if( repaint && "imageUpdate".equals( st.getMethodName() ) )
        {
          imageUpdate = true;
        }
        if( "repaint".equals( st.getMethodName() ) )
        {
          repaint = true;
          fromSwing = false;
        }
      }
      if( imageUpdate )
      {
        //assuming it is java.awt.image.ImageObserver.imageUpdate(...)
        //image was asynchronously updated, that's ok
        return;
      }
      if( repaint && !fromSwing )
      {
        //no problems here, since repaint() is thread safe
        return;
      }
      //ignore the last processed component
      if( m_lastComponent != null && c == m_lastComponent.get() )
      {
        return;
      }
      m_lastComponent = new WeakReference<JComponent>( c );
      violationFound( c, stackTrace );
    }
  }

  protected void violationFound( final JComponent c, final StackTraceElement[] stackTrace )
  {
    System.out.println();
    System.out.println( "EDT violation detected" );
    System.out.println( c );
    for( final StackTraceElement st : stackTrace )
    {
      System.out.println( "\tat " + st );
    }
  }

  public static void main( String[] args ) throws Exception
  {
    //set CheckThreadViolationRepaintManager
    RepaintManager.setCurrentManager( new CheckThreadViolationRepaintManager() );
    //Valid code
    SwingUtilities.invokeAndWait( new Runnable()
    {
      public void run()
      {
        test();
      }
    } );
    System.out.println( "Valid code passed..." );
    repaintTest();
    System.out.println( "Repaint test - correct code" );
    //Invalide code (stack trace expected)
    test();
  }

  static void test()
  {
    JFrame frame = new JFrame( "Am I on EDT?" );
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    frame.add( new JButton( "JButton" ) );
    frame.pack();
    frame.setVisible( true );
    frame.dispose();
  }

  //this test must pass

  static void imageUpdateTest()
  {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    JEditorPane editor = new JEditorPane();
    frame.setContentPane( editor );
    editor.setContentType( "text/html" );
    //it works with no valid image as well
    editor.setText( "<html><img src=\"file:\\lala.png\"></html>" );
    frame.setSize( 300, 200 );
    frame.setVisible( true );
  }

  private static JButton test;

  static void repaintTest()
  {
    try
    {
      SwingUtilities.invokeAndWait( new Runnable()
      {
        public void run()
        {
          test = new JButton();
          test.setSize( 100, 100 );
        }
      } );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    // repaint(Rectangle) should be ok
    test.repaint( test.getBounds() );
    test.repaint( 0, 0, 100, 100 );
    test.repaint();
  }
}