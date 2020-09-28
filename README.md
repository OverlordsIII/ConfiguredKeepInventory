# Configured Keep Inventory

This is a serverside fabric mod that allows you to control how many items leave and stay in your inventory when you die.

The mod uses commands to change config values, but you can change them manually by editing the `inventoryconfig.json5` file located in your minecraft's config directory.
## Features
* item droprate changes (Change the amount of items that fall)
* item saved on death (If specified, a specific item will stay in your inventory no matter the droprate)
* names saved on death (If specified, if the item is named (through an anvil), and the name of the item is in the config, that item will be saved)
* disabling of vanishing curse
* disabling of the binding curse

## Inventory Command
To interact with the mod, do /inventory to access the commands. A full tree of the command is listed below.

* /inventory
    *  set (number from 1 to 100) sets the inventory droprate to a percentage. This percentage is the number of items that drop when you die
    * 

More features coming soon.
