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
To interact with the mod, do /inventory set (percent) the percent is the value of items that will drop when you die. The rest of the items not affected by this will stay in your inventory.

To turn the mod on, do /inventory on

To turn the mod off, do /inventory off

To get the current percent value, do /inventory get

More features coming soon.
