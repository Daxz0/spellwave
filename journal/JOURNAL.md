hello, journaling for fun why not!!


here are some of the basic ideas for this project, just so i can keep it outlined/streamlined and i know what i'm looking at!!

![img.png](img.png)
some of the though processes behind the picture:
- i didnt want to have a spellbook since that would make it wayy too similar to many other magic mods
- i wanted for you to be able to cast spells using ANY item
- i wanted spells to be configurable to the extreme (oof pain)

some central ideas i want to add:

- IMBUEMENTTT:
  - you will be able to imbue stuff, which grants magical properties to the items
  - "spells" are casted via imbuing tablets, and extruders (see later), are able to channel those tablets into spells!
  - you can also imbue other items, such as armor to grant flight etc
    - short vs long imbue
      - i actually hate this feature, originally a timelock but thats not the point of this plugin
- EXTRUDERS
  - these are items that are able to process imbued items and channel them outward

ok lets get to work on the magic system... how do i want to let you imbue things??

i think im stuck in between an inventory UI or like doing it interactive in game
- now looking after ive developed the inventory system, its limited in the way that there arent enough inventory slots in a chest :(
- okay! new idea, you place down the imbuement table as the central block, and then you need a block of amethyst under the table
- then you place smooth stone in a circular arrangement, the bigger this arrangement the more resources you need to feed the table, but the more stuff you can throw into the spell
- i think instead of stone, idk some other block thats magicky
- 

the pros of inventory ui is that its a lot easier to visaulize, but the in world version would just be so much cooler lmao

i think in-world is actually so much harder to implement, as cool as it is, id rather do it in an inventory and then have a super cool animation after in-world


ok. here is the new thing:
![img_1.png](img_1.png)

this is a prototype idea for a spell that is a direct bolt, and then turns into an explosion
- the middle items will determine the element of the spell (in this case fire)
- the idea is that for 60% of the spells lifetime, it will be a direct bolt (as per the amethyst)
- but the last 40% will be an explosion (as per the firework star)
- the feathers will directly speed up the bolts speed
- and then the flowers on the bottom will make the explosion bigger

now the problem i face is, how can i add logic to the spells??
- say i want to explosion to activate only if the bolt hits something
- i could use the imbuement table for this??
- or, a different workstation that allows you to fuse modifiers together, creating new effects when used to well, modify things
- i think thats the better appraoch here