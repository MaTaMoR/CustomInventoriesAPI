package us.swiftex.custominventories.utils.viewer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.swiftex.custominventories.actions.ClickAction;
import us.swiftex.custominventories.icons.Icon;
import us.swiftex.custominventories.icons.NormalIcon;
import us.swiftex.custominventories.inventories.CustomInventory;
import us.swiftex.custominventories.utils.CustomItem;
import us.swiftex.custominventories.utils.InventoryLine;
import us.swiftex.custominventories.utils.InventoryTemplate;
import us.swiftex.custominventories.utils.Size;

import java.util.Iterator;
import java.util.List;

public class ItemViewer extends Pagination<Icon> {

    private final String title;

    public ItemViewer(int pageSize, List<Icon> icons, String title) {
        super(pageSize, icons);
        this.title = title;
    }

    private Icon getBefore(final int page) {
        boolean have = page >= 0 && page < totalPages();

        NormalIcon normalIcon = new NormalIcon(CustomItem.builder(Material.STAINED_CLAY, 1, (short) (have ? 5 : 14))
            .setName((have ? "&a<<" : "&4<<")).build());

        if(have) {
            normalIcon.addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    getView(page).openInventory(player);
                }
            });
        }

        return normalIcon;
    }

    private Icon getNext(final int page) {
        boolean have = page >= 0 && page < totalPages();

        NormalIcon normalIcon = new NormalIcon(CustomItem.builder(Material.STAINED_CLAY, 1, (short) (have ? 5 : 14))
                .setName((have ? "&a>>" : "&4>>")).build());

        if(have) {
            normalIcon.addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    getView(page).openInventory(player);
                }
            });
        }

        return normalIcon;
    }

    public CustomInventory getView(int page) {
        if(page < 0 || page >= totalPages()) return null;

        String replacedTitle = title.replace("{page}", String.valueOf((page + 1))).replace("{total_pages}", String.valueOf(totalPages()));
        Size size = Size.fit(pageSize() + 9);
        InventoryTemplate template = new InventoryTemplate(replacedTitle, size);

        Iterator<InventoryLine> lines = template.getLines().iterator();
        Iterator<Icon> icons = getPage(page).iterator();

        while (lines.hasNext() && icons.hasNext()) {
            InventoryLine line = lines.next();

            int position = 0;
            while (icons.hasNext() && position < 9) {
                line.setIcon(position++, icons.next());
            }
        }

        InventoryLine lastLine = template.getLastLine();

        lastLine.setIcon(0, getBefore(page - 1));
        lastLine.setIcon(8, getNext(page + 1));
        for(int i = 1; 8 > i; i++) {
            lastLine.setIcon(i, new NormalIcon(CustomItem.builder(Material.STAINED_GLASS_PANE, 1, (short) 15).setName("").build()));
        }

        return template.createInventory();
    }
}