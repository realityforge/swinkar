package swinkar.ui;

import java.awt.Component;

public interface MenuContainer
{
  int getItemCount();

  Component getComponent( int i );

  Component add( Component menu, int index );

  void revalidate();

  void remove( int index );

  String getMenuId();
}
