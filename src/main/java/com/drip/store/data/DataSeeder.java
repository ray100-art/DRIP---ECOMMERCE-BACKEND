package com.drip.store.data;

import com.drip.store.model.Product;
import com.drip.store.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
       if (productRepository.count() > 0)  return;

        List<Product> products = Arrays.asList(
                // ── WOMEN ──
                p("Floral Wrap Midi Dress","Zara","Women",11570,15600,"/images/floral-wrap-midi-dress.jpg","new"),
                p("Wide Leg Linen Trousers","H&M","Women",9360,12350,"/images/wide-leg-linen-trousers.jpg","sale"),
                p("Silk Satin Blouse","Zara","Women",12350,0,"/images/silk-satin-blouse.jpg","new"),
                p("Oversized Knit Cardigan","Pull&Bear","Women",14300,18850,"/images/oversized-knit-cardigan.jpg","sale"),
                p("Pleated Mini Skirt","Shein","Women",7540,0,"/images/pleated-mini-skirt.jpg","hot"),
                p("Linen Co-ord Jumpsuit","Mango","Women",17550,0,"/images/linen-coord-jumpsuit.jpg","sale"),
                p("Boho Floral Maxi Dress","ASOS","Women",10270,13650,"/images/boho-floral-maxi-dress.jpg","new"),
                p("Power Shoulder Blazer Dress","Zara","Women",20540,27300,"/images/power-shoulder-blazer-dress.jpg","hot"),
                p("Ribbed Bodycon Mini Dress","PrettyLittleThing","Women",8580,11440,"/images/ribbed-bodycon-mini-dress.jpg","new"),
                p("Satin Slip Evening Dress","ASOS","Women",15470,19890,"/images/satin-slip-evening-dress.jpg","hot"),
                p("Cropped Denim Jacket","Levi's","Women",16900,22100,"/images/cropped-denim-jacket-women.jpg","new"),
                p("High Waist Mom Jeans","Levi's","Women",13520,17550,"/images/high-waist-mom-jeans.jpg","sale"),
                p("Printed Wrap Blouse","H&M","Women",8190,0,"/images/printed-wrap-blouse.jpg","new"),
                p("Velvet Midi Slip Dress","Zara","Women",18200,23400,"/images/velvet-midi-slip-dress.jpg","hot"),
                p("Trench Coat Classic","Burberry","Women",45500,58500,"/images/trench-coat-classic.jpg","sale"),
                p("Corset Top Bralette","Shein","Women",5720,7800,"/images/corset-top-bralette.jpg","hot"),
                p("Campus Oversized Hoodie","Champion","Women",9100,12350,"/images/campus-oversized-hoodie.jpg","new"),
                p("Leather Mini Skirt","Zara","Women",12870,16900,"/images/pleated-mini-skirt.jpg","hot"),
                p("Printed Maxi Wrap Skirt","Mango","Women",10920,0,"/images/printed-maxi-wrap-skirt.jpg","new"),
                p("Puffer Quilted Jacket","The North Face","Women",28600,36400,"/images/puffer-quilted-jacket-women.jpg","sale"),
                // ── MEN ──
                p("Slim Fit Chino Trousers","Zara Man","Men",11050,14300,"/images/slim-fit-chino-trousers.jpg","sale"),
                p("Oxford Button-Down Shirt","Ralph Lauren","Men",8840,0,"/images/oxford-button-down-shirt.jpg","new"),
                p("Premium Leather Jacket","AllSaints","Men",38350,49400,"/images/premium-leather-jacket.jpg","hot"),
                p("Merino Wool Crew Sweater","Uniqlo","Men",16640,21450,"/images/merino-wool-crew-sweater.jpg","sale"),
                p("Cargo Utility Shorts","Carhartt","Men",8060,10400,"/images/cargo-utility-shorts.jpg","new"),
                p("Linen Summer Shirt","Zara Man","Men",10140,13000,"/images/linen-summer-shirt.jpg","sale"),
                p("Classic Denim Jacket","Levi's","Men",18850,24700,"/images/classic-denim-jacket-men.jpg","hot"),
                p("Tech Fleece Track Suit","Nike","Men",21840,28600,"/images/tech-fleece-track-suit.jpg","new"),
                p("2-Piece Business Suit","Hugo Boss","Men",65000,84500,"/images/2-piece-business-suit.jpg","hot"),
                p("Slim Fit Tuxedo Suit","Calvin Klein","Men",72800,91000,"/images/wide-leg-linen-trousers.jpg","new"),
                p("Campus Graphic Hoodie","Adidas","Men",9750,13000,"/images/campus-oversized-hoodie.jpg","hot"),
                p("Oversized Streetwear Tee","Supreme","Men",7150,0,"/images/oversized-streetwear-tee.jpg","new"),
                p("Slim Chino Suit Trousers","Hugo Boss","Men",18200,23400,"/images/slim-chino-suit-trousers.jpg","sale"),
                p("Puffer Down Jacket","The North Face","Men",32500,42250,"/images/puffer-down-jacket-men.jpg","sale"),
                p("Campus Jogger Pants","Adidas","Men",8450,11050,"/images/campus-jogger-pants.jpg","new"),
                p("Flannel Check Overshirt","Carhartt","Men",12350,16250,"/images/flannel-check-overshirt.jpg","hot"),
                p("Polo Shirt Classic Fit","Ralph Lauren","Men",10400,13520,"/images/polo-shirt-classic-fit.jpg","sale"),
                p("Washed Denim Jeans","Levi's","Men",15470,20150,"/images/washed-denim-jeans.jpg","new"),
                p("Bomber Flight Jacket","Alpha Industries","Men",24700,31850,"/images/bomber-flight-jacket.jpg","hot"),
                p("Formal Dress Shirt White","Calvin Klein","Men",9100,11700,"/images/oxford-button-down-shirt.jpg","sale"),
                // ── SHOES ──
                p("Adidas Forum Low Sneakers","Adidas","Shoes",14300,18200,"/images/adidas-forum-low.jpg","hot"),
                p("Nike Air Force 1 White","Nike","Shoes",16250,20800,"/images/nike-air-force-1.jpg","hot"),
                p("Adidas Stan Smith","Adidas","Shoes",13000,16900,"/images/adidas-stan-smith.jpg","sale"),
                p("Nike Air Max 270","Nike","Shoes",22100,28600,"/images/nike-air-max-270.jpg","new"),
                p("Chelsea Ankle Boots","Dr. Martens","Shoes",25350,32500,"/images/chelsea-ankle-boots.jpg","hot"),
                p("Platform Block Heel Sandals","Steve Madden","Shoes",12350,15990,"/images/platform-block-heel-sandals.jpg","new"),
                p("New Balance 574 Classic","New Balance","Shoes",17550,22750,"/images/adidas-stan-smith.jpg","sale"),
                p("Converse Chuck Taylor High","Converse","Shoes",11700,14950,"/images/converse-chuck-taylor-high.jpg","hot"),
                p("Leather Oxford Dress Shoes","Clarks","Shoes",19500,25350,"/images/leather-oxford-dress-shoes.jpg","sale"),
                p("Vans Old Skool Classic","Vans","Shoes",10400,13520,"/images/vans-old-skool-classic.jpg","new"),
                p("Puma Suede Classic","Puma","Shoes",11050,14300,"/images/puma-suede-classic.jpg","hot"),
                p("Timberland 6-Inch Boots","Timberland","Shoes",26000,33800,"/images/timberland-6inch-boots.jpg","sale"),
                p("Gucci Horsebit Loafers","Gucci","Shoes",48100,62400,"/images/gucci-horsebit-loafers.jpg","hot"),
                p("Nike Pegasus Running Shoes","Nike","Shoes",19500,25350,"/images/nike-air-force-1.jpg","new"),
                p("Adidas Ultraboost 22","Adidas","Shoes",24700,32500,"/images/adidas-forum-low.jpg","hot"),
                p("Vans Slip-On Canvas","Vans","Shoes",8450,10920,"/images/vans-old-skool-classic.jpg","sale"),
                p("Stiletto Pointed Heels","Steve Madden","Shoes",14950,19500,"/images/platform-block-heel-sandals.jpg","new"),
                p("Jordan 1 Retro High OG","Jordan","Shoes",29900,38350,"/images/jordan-1-retro-high.jpg","hot"),
                // ── WATCHES ──
                p("Casio G-Shock DW-5600","Casio","Watches",13000,16900,"/images/casio-gshock-dw5600.jpg","hot"),
                p("Seiko 5 Sports Automatic","Seiko","Watches",22100,28600,"/images/seiko-5-sports-automatic.jpg","new"),
                p("Daniel Wellington Classic","Daniel Wellington","Watches",19500,25350,"/images/daniel-wellington-classic.jpg","sale"),
                p("Fossil Gen 6 Smartwatch","Fossil","Watches",32500,42250,"/images/fossil-gen6-smartwatch.jpg","new"),
                p("Casio Vintage Gold A168","Casio","Watches",9100,11700,"/images/casio-vintage-gold.jpg","hot"),
                p("Tommy Hilfiger Classic","Tommy Hilfiger","Watches",16250,21060,"/images/tommy-hilfiger-classic-watch.jpg","sale"),
                p("Citizen Eco-Drive Solar","Citizen","Watches",28600,37050,"/images/citizen-eco-drive-solar.jpg","new"),
                p("Nixon Time Teller","Nixon","Watches",14300,18590,"/images/nixon-time-teller.jpg","hot"),
                p("Armani Exchange Stainless","Armani Exchange","Watches",24700,32110,"/images/armani-exchange-stainless.jpg","sale"),
                p("Garmin Forerunner 255","Garmin","Watches",45500,58500,"/images/garmin-forerunner-255.jpg","new"),
                p("Casio F-91W Digital","Casio","Watches",3250,4550,"/images/casio-f91w-digital.jpg","sale"),
                p("Michael Kors Rose Gold","Michael Kors","Watches",35100,45500,"/images/michael-kors-rose-gold.jpg","hot"),
                // ── ACCESSORIES ──
                p("Leather Tote Shoulder Bag","Coach","Accessories",23140,30030,"/images/leather-tote-shoulder-bag.jpg","hot"),
                p("Silk Printed Square Scarf","Gucci","Accessories",8450,10920,"/images/silk-printed-square-scarf.jpg","sale"),
                p("Mini Crossbody Bag","Coach","Accessories",17550,22750,"/images/mini-crossbody-bag.jpg","new"),
                p("Snapback Cap Streetwear","New Era","Accessories",5850,7800,"/images/snapback-cap-streetwear.jpg","hot"),
                p("Gold Cuban Link Chain","DRIP Gold","Accessories",11570,15080,"/images/gold-cuban-link-chain.jpg","hot"),
                p("Aviator Sunglasses","Ray-Ban","Accessories",22100,28730,"/images/aviator-sunglasses.jpg","sale"),
                p("Leather Belt Braided","Tommy Hilfiger","Accessories",6500,8450,"/images/leather-belt-braided.jpg","new"),
                p("Canvas Backpack Campus","Herschel","Accessories",14300,18590,"/images/leather-belt-braided.jpg","hot"),
                p("Gold Hoop Earrings Set","DRIP Gold","Accessories",4550,5850,"/images/gold-hoop-earrings-set.jpg","new"),
                p("Waist Fanny Pack","Adidas","Accessories",5200,6760,"/images/leather-tote-shoulder-bag.jpg","sale"),
                p("Beanie Knit Winter Hat","Carhartt","Accessories",4160,5460,"/images/beanie-knit-winter-hat.jpg","new"),
                p("Cat Eye Sunglasses","Prada","Accessories",29900,38870,"/images/aviator-sunglasses.jpg","hot"),
                p("Leather Card Holder Wallet","Coach","Accessories",7150,9295,"/images/leather-card-holder-wallet.jpg","sale"),
                p("Straw Bucket Hat Summer","H&M","Accessories",4550,5850,"/images/straw-bucket-hat-summer.jpg","new"),
                p("Statement Pearl Necklace","DRIP Gold","Accessories",8840,11440,"/images/statement-pearl-necklace.jpg","hot"),
                // ── KIDS ──
                p("Kids Denim Bib Set","H&M Kids","Kids",7150,9295,"/images/kids-denim-bib-set.jpg","new"),
                p("Girls Tutu Party Dress","Zara Kids","Kids",6240,8190,"/images/girls-tutu-party-dress.jpg","hot"),
                p("Kids Air Max Sneakers","Nike Kids","Kids",8060,10400,"/images/nike-air-force-1.jpg","sale"),
                p("Boys Zip-Up Hoodie","Adidas Kids","Kids",5460,6760,"/images/kids-denim-bib-set.jpg","new"),
                p("Girls Floral Sundress","H&M Kids","Kids",5850,7540,"/images/girls-tutu-party-dress.jpg","hot"),
                p("Boys Cargo Shorts","Zara Kids","Kids",4550,5980,"/images/kids-denim-bib-set.jpg","sale"),
                p("Kids School Backpack","Adidas Kids","Kids",6500,8450,"/images/leather-belt-braided.jpg","new"),
                p("Baby Onesie Set 3-Pack","H&M Baby","Kids",3900,5070,"/images/kids-denim-bib-set.jpg","hot")
        );

        productRepository.saveAll(products);
        System.out.println("✅ Seeded " + products.size() + " products");
    }

    private Product p(String name, String brand, String category, double price, double oldPrice, String imageUrl, String badge) {
        Product pr = new Product();
        pr.setName(name);
        pr.setBrand(brand);
        pr.setCategory(category);
        pr.setPrice(price);
        pr.setOldPrice(oldPrice > 0 ? oldPrice : null);
        pr.setImageUrl(imageUrl);
        pr.setBadge(badge);
        return pr;
    }
}