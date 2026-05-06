package com.example.laptoparthesis.data

data class LaptopModel(
    val id: String,
    val name: String,
    val brand: String,
    val specs: Map<String, String>,
    val description: String,
    val category: String
)

data class HardwareComponent(
    val id: String,
    val name: String,
    val type: String,
    val specs: Map<String, String>,
    val description: String
)

object MockData {
    val laptops = listOf(
        LaptopModel(
            id = "dell-xps-15",
            name = "XPS 15",
            brand = "Dell",
            category = "Ultrabook",
            description = "The Dell XPS 15 is a high-end laptop with a stunning InfinityEdge display and powerful performance.",
            specs = mapOf(
                "Processor" to "Intel Core i7-13700H",
                "RAM" to "16GB DDR5",
                "GPU" to "NVIDIA RTX 4050 6GB",
                "Storage" to "512GB NVMe SSD",
                "Display" to "15.6\" OLED 3.5K"
            )
        ),
        LaptopModel(
            id = "mbp-14-m3",
            name = "MacBook Pro 14",
            brand = "Apple",
            category = "Professional",
            description = "The MacBook Pro 14 with M3 Pro chip delivers extreme performance and amazing battery life.",
            specs = mapOf(
                "Processor" to "Apple M3 Pro (11-core)",
                "RAM" to "18GB Unified Memory",
                "GPU" to "14-core Apple GPU",
                "Storage" to "512GB SSD",
                "Display" to "14.2\" Liquid Retina XDR"
            )
        ),
        LaptopModel(
            id = "rog-zephyrus-g14",
            name = "ROG Zephyrus G14",
            brand = "ASUS",
            category = "Gaming",
            description = "A powerful 14-inch gaming laptop that balances portability with top-tier performance.",
            specs = mapOf(
                "Processor" to "AMD Ryzen 9 7940HS",
                "RAM" to "16GB DDR5",
                "GPU" to "NVIDIA RTX 4070 8GB",
                "Storage" to "1TB NVMe SSD",
                "Display" to "14\" QHD+ 165Hz"
            )
        )
    )

    val components = listOf(
        HardwareComponent(
            id = "rtx-4050-mobile",
            name = "NVIDIA RTX 4050",
            type = "GPU",
            description = "A mid-range mobile graphics card based on the Ada Lovelace architecture.",
            specs = mapOf(
                "Cuda Cores" to "2560",
                "Memory" to "6GB GDDR6",
                "Power" to "35W - 115W TGP",
                "DLSS" to "Version 3.0 Supported"
            )
        ),
        HardwareComponent(
            id = "i7-13700h",
            name = "Intel Core i7-13700H",
            type = "CPU",
            description = "High-end mobile CPU for laptops based on the Raptor Lake architecture.",
            specs = mapOf(
                "Cores" to "14 (6P + 8E)",
                "Threads" to "20",
                "Max Clock" to "5.0 GHz",
                "Cache" to "24MB L3"
            )
        ),
        HardwareComponent(
            id = "samsung-980-pro",
            name = "Samsung 980 Pro",
            type = "SSD",
            description = "One of the fastest PCIe 4.0 NVMe SSDs available for modern laptops.",
            specs = mapOf(
                "Interface" to "PCIe Gen 4.0 x4",
                "Capacity" to "1TB / 2TB",
                "Read Speed" to "Up to 7,000 MB/s",
                "Write Speed" to "Up to 5,000 MB/s"
            )
        )
    )
}
