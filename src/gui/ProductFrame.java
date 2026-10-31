package gui;

import dao.ProductDAO;
import util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class ProductFrame extends JFrame {

    private final String username;
    private CartFrame cartFrame;

    private final ProductDAO productDAO = new ProductDAO();

    private List<Object[]> allProducts = new ArrayList<>();

    private final JPanel productPanel = new JPanel();

    private final JTextField searchField = new JTextField(20);

    private final JComboBox<String> categoryBox =
            new JComboBox<>(new String[]{"All Categories"});

    private boolean updatingCategories = false;

    public ProductFrame(String username) {

        this.username = username;

        setTitle("ShopEase | Online Shopping");
        setSize(1200, 780);
        setMinimumSize(new Dimension(900, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadProductsFromDatabase();
    }

    // ================= HEADER =================

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(UITheme.WHITE);
        header.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0,
                        new Color(225, 230, 238)),
                new EmptyBorder(18, 30, 18, 30)
        ));

        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(
                brandPanel, BoxLayout.Y_AXIS));

        JLabel brand = new JLabel("ShopEase");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 28));
        brand.setForeground(UITheme.PRIMARY_DARK);

        JLabel tagline = new JLabel("YOUR SHOPPING DESTINATION");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        tagline.setForeground(UITheme.MUTED);

        brandPanel.add(brand);
        brandPanel.add(tagline);

        JLabel welcome = new JLabel("Welcome, " + username);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 13));
        welcome.setForeground(UITheme.TEXT);

        JButton cartButton = UITheme.createButton("View Cart");
        JButton historyButton =
                UITheme.createButton("Order History");

        JButton logoutButton =
                UITheme.createDangerButton("Logout");

        cartButton.addActionListener(e -> openCart());

        historyButton.addActionListener(e ->
                new OrderHistoryFrame(username).setVisible(true));

        logoutButton.addActionListener(e -> {

            if (cartFrame != null) {
                cartFrame.dispose();
                cartFrame = null;
            }

            dispose();
            new LoginFrame().setVisible(true);
        });

        JPanel rightPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 5));

        rightPanel.setOpaque(false);

        rightPanel.add(welcome);
        rightPanel.add(cartButton);
        rightPanel.add(historyButton);
        rightPanel.add(logoutButton);

        header.add(brandPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    // ================= MAIN CONTENT =================

    private JPanel createMainContent() {

        JPanel main = new JPanel(new BorderLayout(20, 22));

        main.setBackground(UITheme.BACKGROUND);
        main.setBorder(new EmptyBorder(28, 35, 30, 35));

        // Heading

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(
                topPanel, BoxLayout.Y_AXIS));

        topPanel.setOpaque(false);

        JLabel heading = UITheme.createTitle("Discover Products");

        JLabel subtitle = new JLabel(
                "Explore our collection and find something you love."
        );

        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(UITheme.MUTED);

        topPanel.add(heading);
        topPanel.add(Box.createVerticalStrut(7));
        topPanel.add(subtitle);

        topPanel.add(Box.createVerticalStrut(22));

        // Search and filters

        JPanel filterPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 12, 5));

        filterPanel.setBackground(UITheme.WHITE);

        filterPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(225, 230, 238), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        UITheme.styleTextField(searchField);

        searchField.setPreferredSize(
                new Dimension(250, 40));

        searchField.setToolTipText("Search products...");

        categoryBox.setFont(
                new Font("Segoe UI", Font.PLAIN, 13));

        categoryBox.setPreferredSize(
                new Dimension(180, 40));

        JButton searchButton =
                UITheme.createButton("Search");

        JButton clearButton =
                UITheme.createButton("Clear");

        JButton refreshButton =
                UITheme.createButton("Refresh");

        searchButton.addActionListener(e -> refreshProducts());

        searchField.addActionListener(e -> refreshProducts());

        categoryBox.addActionListener(e -> {

            if (!updatingCategories) {
                refreshProducts();
            }
        });

        clearButton.addActionListener(e -> {

            searchField.setText("");
            categoryBox.setSelectedIndex(0);

            refreshProducts();
        });

        refreshButton.addActionListener(e ->
                loadProductsFromDatabase());

        filterPanel.add(searchField);
        filterPanel.add(categoryBox);
        filterPanel.add(searchButton);
        filterPanel.add(clearButton);
        filterPanel.add(refreshButton);

        topPanel.add(filterPanel);

        // Product grid

        productPanel.setLayout(
                new GridLayout(0, 3, 20, 20));

        productPanel.setBackground(UITheme.BACKGROUND);

        productPanel.setBorder(
                new EmptyBorder(5, 3, 15, 3));

        JScrollPane scrollPane = new JScrollPane(productPanel);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        scrollPane.getViewport().setBackground(
                UITheme.BACKGROUND);

        scrollPane.getVerticalScrollBar().setUnitIncrement(18);

        main.add(topPanel, BorderLayout.NORTH);
        main.add(scrollPane, BorderLayout.CENTER);

        return main;
    }

    // ================= LOAD PRODUCTS =================

    private void loadProductsFromDatabase() {

        allProducts = productDAO.getAllProducts();

        updateCategories();

        refreshProducts();
    }

    // ================= CATEGORIES =================

    private void updateCategories() {

        String selected =
                (String) categoryBox.getSelectedItem();

        Set<String> categories = new LinkedHashSet<>();

        for (Object[] product : allProducts) {
            categories.add(String.valueOf(product[2]));
        }

        updatingCategories = true;

        categoryBox.removeAllItems();

        categoryBox.addItem("All Categories");

        for (String category : categories) {
            categoryBox.addItem(category);
        }

        if (selected != null) {
            categoryBox.setSelectedItem(selected);
        }

        if (categoryBox.getSelectedIndex() == -1) {
            categoryBox.setSelectedIndex(0);
        }

        updatingCategories = false;
    }

    // ================= SEARCH AND FILTER =================

    private void refreshProducts() {

        productPanel.removeAll();

        String query = searchField.getText()
                .trim().toLowerCase();

        String selectedCategory =
                String.valueOf(categoryBox.getSelectedItem());

        int count = 0;

        for (Object[] product : allProducts) {

            String name = String.valueOf(product[1]);
            String category = String.valueOf(product[2]);

            boolean matchesName =
                    name.toLowerCase().contains(query);

            boolean matchesCategory =
                    selectedCategory.equals("All Categories")
                    || category.equals(selectedCategory);

            if (matchesName && matchesCategory) {

                productPanel.add(createProductCard(product));

                count++;
            }
        }

        if (count == 0) {

            JPanel emptyPanel = new JPanel(
                    new GridBagLayout());

            emptyPanel.setBackground(UITheme.BACKGROUND);

            JLabel message = new JLabel(
                    "No products found. Try another search."
            );

            message.setFont(
                    new Font("Segoe UI", Font.PLAIN, 16));

            message.setForeground(UITheme.MUTED);

            emptyPanel.add(message);

            productPanel.setLayout(new BorderLayout());

            productPanel.add(emptyPanel, BorderLayout.CENTER);

        } else {

            productPanel.setLayout(
                    new GridLayout(0, 3, 20, 20));
        }

        productPanel.revalidate();
        productPanel.repaint();
    }

    // ================= PRODUCT CARD =================

    private JPanel createProductCard(Object[] product) {

        int id = ((Number) product[0]).intValue();

        String name = String.valueOf(product[1]);

        String category = String.valueOf(product[2]);

        double price =
                ((Number) product[3]).doubleValue();

        int stock =
                ((Number) product[4]).intValue();

        JPanel card = new JPanel();

        card.setLayout(new BoxLayout(
                card, BoxLayout.Y_AXIS));

        card.setBackground(UITheme.WHITE);

        card.setBorder(new CompoundBorder(
                new LineBorder(
                        new Color(225, 230, 238), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Product icon area

        JPanel iconPanel = new JPanel(
                new GridBagLayout());

        iconPanel.setBackground(
                new Color(239, 245, 252));

        iconPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 105));

        iconPanel.setPreferredSize(
                new Dimension(200, 105));

        JLabel icon = new JLabel("PRODUCT");

        icon.setFont(
                new Font("Segoe UI", Font.BOLD, 19));

        icon.setForeground(UITheme.PRIMARY);

        iconPanel.add(icon);

        // Category

        JLabel categoryLabel = new JLabel(category);

        categoryLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 12));

        categoryLabel.setForeground(UITheme.MUTED);

        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Name

        JLabel nameLabel = new JLabel(name);

        nameLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 18));

        nameLabel.setForeground(UITheme.TEXT);

        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Price

        JLabel priceLabel = new JLabel(
                String.format("\u20B9%.2f", price));

        priceLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 21));

        priceLabel.setForeground(UITheme.PRIMARY);

        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Stock

        JLabel stockLabel = new JLabel(
                stock > 0
                ? "●  In Stock: " + stock
                : "●  Out of Stock"
        );

        stockLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 12));

        stockLabel.setForeground(
                stock > 0
                ? UITheme.SUCCESS
                : UITheme.DANGER
        );

        stockLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add to cart

        JButton addButton =
                UITheme.createButton("Add to Cart");

        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        addButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 43));

        addButton.setEnabled(stock > 0);

        addButton.addActionListener(e -> {

            if (cartFrame == null ||
                    !cartFrame.isDisplayable()) {

                cartFrame = new CartFrame(username);
            }

            cartFrame.addProduct(name, price);

            JOptionPane.showMessageDialog(
                    this,
                    name + " added to your cart!",
                    "Added to Cart",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        // Assemble card

        card.add(iconPanel);

        card.add(Box.createVerticalStrut(18));

        card.add(categoryLabel);

        card.add(Box.createVerticalStrut(8));

        card.add(nameLabel);

        card.add(Box.createVerticalStrut(13));

        card.add(priceLabel);

        card.add(Box.createVerticalStrut(10));

        card.add(stockLabel);

        card.add(Box.createVerticalGlue());

        card.add(Box.createVerticalStrut(18));

        card.add(addButton);

        return card;
    }

    // ================= CART =================

    private void openCart() {

        if (cartFrame == null ||
                !cartFrame.isDisplayable()) {

            cartFrame = new CartFrame(username);
        }

        cartFrame.setVisible(true);

        cartFrame.toFront();
    }
}