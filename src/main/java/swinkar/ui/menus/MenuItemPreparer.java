package swinkar.ui.menus;

import java.awt.Component;

/**
 * Interface for a component that can prepare a MenuItem prior to it's being displayed
 */
public interface MenuItemPreparer
{
  /**
   * Prepare the given MenuItem for display on the given Menu
   */
  void prepareMenuItem( MenuItem item, MenuContainer menu );
}
