# Configured Keep Inventory

This is a serverside fabric mod that allows you to control how many items leave and stay in your inventory when you die. [Find the latest releases here](https://www.curseforge.com/minecraft/mc-mods/configured-keep-inventory)

The mod uses commands to change config values, but you can change them manually by editing the `inventoryconfig.json5` file located in your minecraft's config directory.
## Features
* item droprate changes (Change the amount of items that fall)
* item saved on death (If specified, a specific item will stay in your inventory no matter the droprate)
* names saved on death (If specified, if the item is named (through an anvil), and the name of the item is in the config, that item will be saved)
* Allows you to autofill your hunger if food is in your inventory
* Totems activate anywhere in your inventory on death, not just in your hand/offhand
* Players can teleport where a player has died or used a totem to help them out. (Disabled by default)
* disabling of vanishing curse
* disabling of the binding curse
* Serverside command system to configure the mod
* Mod Menu integration for integrated server (i.e singleplayer worlds)
* Allows totems to work even if you /kill or fall into the void

## Inventory Command
To interact with the mod, do /inventory to access the commands. A full tree of the command is listed below.

* /inventory
    *  set (number from 1 to 100) : sets the inventory droprate to a percentage. This percentage is the number of items that drop when you die
    *  on/off : turns the mod off or on 
    *  info : gives you all the current config values for the mod
    * add 
        * name (name): adds a name to the name save list. Items named with this name will not drop when you die
        * item (item): adds an item to the item save list. These items will not drop when you die
    * remove
        * name (name) : removes a name from the name save list
        * item (item) : removes an item from the item save list
    * disable
        * vanishing : turns off the vanishing curse
        * binding : turns off the binding curse
    * enable
        * vanishing: reenables the vanishing curse
        * binding : reenables the binding curse
        
More features coming soon.
