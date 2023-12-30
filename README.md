# FarmingPlus
What is **FarmingPlus**?
  - FarmingPlus is a Minecraft farming plugin that adds new mechanics to farming.
  - It's a free plugin that is easy to use and configure.
  - It's compatible with the next plugins:
     - Vault
     - WorldGuard
     - MythicMobs
     - PlaceholderAPI
# Overview
FarmingPlus is a plugin that allows players to experience new mechanics in farming. It includes new enchantments, with their own enchanting GUI, rewards for farming, and more.
# Features
  - **Enchantments**:
    - Farmer's Grace: Prevent crops from being destroyed when trampled. **(Boots)**
    - Replenish: Allows the player to replant crops automatically. **(Hoe/Axe)**
    - Delicate: Prevents stems from being destroyed. **(Axe)**
    - Irrigate: Allows the player to irrigate 64 blocks in a row (Configurable in config.yml). **(Water Bucket)**
    - Grand Tilling: Allows the player to till an area. **(Hoe)**
      - Level I: 3x3 area.
      - Level II: 5x5 area.
      - Level III: 64 blocks in a row (Configurable in config.yml).
    - Farmer's Step: Allow the player to plant crops automatically from their inventories in the area where they move. **(Boots)**
      - Level I: 3x3 area.
      - Level II: 5x5 area.
      - Level III: 7x7 area.
  - **Enchanting GUI**:
    - Enchant your tools with the enchanting GUI. Configure the costs in config.yml. They can be money, experience and items.
    - Enchant with /fp gui.
  - **Rewards**:
    Configure how much rewards you want in rewards.yml. By default, there are examples in the file.
    The leaderboard saves the record of when and what rewards the player has received. It's saved in Json file every few minutes(Configurable in config.yml).
    - They work with the next parameters:
      - crops: 
      - type:
      - chance
      - messages:
      - sound:
    - Depending on the type there are different extra parameters:
      - Money:  
        - amount: 
      - Item:
        - items:
      - Command:
        - commands:
      - Summon:
        Choose the mob that you want to summon. You can use MythicMobs.
        - mob:
        - amount:
        - level:
# Permissions
  - fp.commands.reload: Reloading config.yml.
  - fp.commands.enchant: Let the player enchant with /fp enchant to enchant without costs (ADMIN).
  - fp.commands.reward: Let the player use /fp reward.
  - fp.gui.use: Let the player use the GUI for enchanting.
  - fp.bypass.farmerstep: Plants not needed in inventory when using Farmer's Step.
  - fp.bypass.farmerstep.protection: Let the player use Farmer's step on WorldGuard regions. 
  - fp.bypass.replenish.protection: Let the player use Replenish on WorldGuard regions.
  - fp.bypass.grandtilling.protection: Let the player use Grand Tilling on WorldGuard regions.
  - fp.bypass.irrigate.protection: Let the player use Irrigate on WorldGuard regions.
  - fp.bypass.durability-damage: Tools or boots don't take durability damage when used by that player.
  - fp.admin: Administrative permissions.
# Compatibilities
- **Vault**:
  - Setup economic costs when enchanting items.
  - Setup economic rewards.

- **WorldGuard**:
  - Region control for Farmer's Step, Replenish, Irrigate and rewards.

- **MythicMobs**:
  - You can summon MythicMobs as rewards.

- **PlaceholderAPI**:
  - You can use placeholders in others plugins.

# PlaceholderAPI placeholders
  - %farmingplus_prefix%: Prefix of the plugin, configurable in config.yml.
  - %farmingplus_total_rewards_top_number%: Top **number** player in rewards leaderboard. Change **number** for the top that you want to access.
  - %farmingplus_top_position%: Position of the player in the leaderboard.
  - %farmingplus_total_rewards%: Total rewards of the player.
  
Only working in the plugin:
  - %farmingplus_top_number%: Changes to the number top that is unfilled. Used for **not-top-reward** in config.yml.
  
# Discord
If you have any questions, found a bug or want to suggest something, you can join to the official [Discord Server](https://discord.gg/2KhE6xeEnf).
