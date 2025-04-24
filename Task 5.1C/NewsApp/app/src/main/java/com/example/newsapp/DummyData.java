package com.example.newsapp;

import java.util.ArrayList;
import java.util.List;

public class DummyData {

    public static List<NewsItem> getDummyTopStories() {
        List<NewsItem> list = new ArrayList<>();

        list.add(new NewsItem("AI Breakthrough", R.drawable.ai_breakthrough,
                "Scientists have unveiled a groundbreaking artificial intelligence system that closely mimics human cognitive functions. " +
                        "This model is capable of reasoning, problem-solving, and adapting to new tasks with minimal data.\n\n" +
                        "Unlike traditional AIs, this system was trained using a neurosymbolic approach that blends deep learning with logical inference. " +
                        "Early tests show improved understanding in language, vision, and decision-making tasks.\n\n" +
                        "Experts predict this could revolutionize fields such as healthcare, education, and scientific research, by making machines truly assistive in everyday problem-solving."));

        list.add(new NewsItem("SpaceX Launch", R.drawable.space_launch,
                "SpaceX successfully launched a Falcon 9 rocket today, deploying a communications satellite into geostationary orbit. " +
                        "The launch marks the company’s 75th consecutive successful mission.\n\n" +
                        "Elon Musk announced that the reusable booster landed safely on the ocean barge 'Of Course I Still Love You', showcasing the advancement of low-cost space travel.\n\n" +
                        "This mission is a stepping stone toward the Mars colonization initiative, and a demonstration of high-frequency launches using minimal recovery time."));

        list.add(new NewsItem("Climate Alert", R.drawable.climate_report,
                "The UN released its latest climate report warning that global temperatures may rise beyond 2°C in the next 20 years if no action is taken.\n\n" +
                        "Key findings show rising sea levels, increased frequency of natural disasters, and significant loss of biodiversity. Developing countries are expected to face the worst effects.\n\n" +
                        "The report urges governments to cut emissions by 50% before 2030 and transition to clean energy at an unprecedented pace."));

        list.add(new NewsItem("Tech Giants Merge", R.drawable.tech_giants,
                "In a historic move, two of the world’s leading tech corporations have agreed to a merger valued at $500 billion.\n\n" +
                        "The newly formed conglomerate aims to dominate AI, cloud services, and digital infrastructure across all major markets.\n\n" +
                        "Regulators are expected to conduct antitrust reviews, but analysts believe the merger could redefine tech industry standards and innovation."));

        list.add(new NewsItem("Mars Rover Discovery", R.drawable.mars_rover,
                "NASA’s Perseverance rover has uncovered organic compounds in the Jezero Crater on Mars, potentially pointing to ancient microbial life.\n\n" +
                        "The rover used its onboard spectrometer to analyze rock samples dating back 3 billion years.\n\n" +
                        "Future sample return missions may confirm whether these compounds are biological or geological, making this one of the most exciting discoveries in planetary science."));

        list.add(new NewsItem("Cybersecurity Breach", R.drawable.security_br,
                "A major international bank reported a data breach compromising over 100 million user accounts across 30 countries.\n\n" +
                        "Hackers exploited a zero-day vulnerability in a third-party cloud service, gaining access to sensitive personal and financial data.\n\n" +
                        "Authorities are investigating, while the company rolls out emergency patches and notifies affected users with steps to secure their accounts."));

        return list;
    }

    public static List<NewsItem> getDummyLatestNews() {
        List<NewsItem> list = new ArrayList<>();

        list.add(new NewsItem("Tech Expo 2025", R.drawable.tech_conf,
                "Tech Expo 2025 drew record-breaking crowds in Tokyo, showcasing futuristic AI robots, AR experiences, and sustainable gadgets.\n\n" +
                        "Keynote speeches from tech leaders emphasized the role of quantum computing, 6G networks, and climate-aware innovation.\n\n" +
                        "Startups from 45 countries displayed prototypes that bridge digital and real-world interaction in transformative ways."));

        list.add(new NewsItem("Electric Car Boom", R.drawable.electric,
                "Electric vehicles (EVs) have surpassed internal combustion cars in global sales for the first time in history.\n\n" +
                        "Tesla, BYD, and several emerging Asian automakers are dominating EV markets, spurred by government incentives and improved battery tech.\n\n" +
                        "Experts say this marks the beginning of a transportation revolution that will impact oil, logistics, and urban infrastructure."));

        list.add(new NewsItem("Healthcare AI", R.drawable.health,
                "AI in healthcare is revolutionizing diagnostics and patient care. Recent tools are detecting cancer, heart disease, and infections with near-perfect accuracy.\n\n" +
                        "Hospitals using AI-based triage systems have reported faster treatment and fewer diagnostic errors.\n\n" +
                        "While adoption is still early in some regions, major healthcare providers are investing heavily in AI-driven platforms."));

        list.add(new NewsItem("Global Economic Shift", R.drawable.economy,
                "Markets across Asia and Europe are reacting to a shift in economic power driven by AI, digital trade, and climate finance.\n\n" +
                        "The IMF predicts emerging economies will lead global GDP growth over the next decade.\n\n" +
                        "As inflation eases and tech adoption grows, new players are emerging in the financial landscape."));

        list.add(new NewsItem("Smart Cities Rising", R.drawable.smart_city,
                "Smart cities are being built with integrated IoT, clean transit, and sustainable energy sources.\n\n" +
                        "Tokyo, Dubai, and Singapore have deployed smart grid technologies that optimize power, water, and traffic flows.\n\n" +
                        "Urban innovation hubs are also focusing on inclusivity, digital access, and AI-driven governance."));

        list.add(new NewsItem("5G Expansion", R.drawable.ggg,
                "Over 80% of urban centers now have access to high-speed 5G connectivity, according to the GSMA’s 2025 report.\n\n" +
                        "This has spurred growth in mobile gaming, video conferencing, and autonomous vehicles.\n\n" +
                        "Governments are now prioritizing rural 5G infrastructure to close the digital divide."));

        list.add(new NewsItem("Quantum Computing Breakthrough", R.drawable.quant,
                "Scientists have achieved a new milestone in quantum stability, keeping qubits intact for over 30 seconds.\n\n" +
                        "This improvement enables more accurate calculations and real-world applications in logistics and drug discovery.\n\n" +
                        "With commercial quantum machines expected in the next 5 years, the tech world is entering a new age of computing."));

        list.add(new NewsItem("New Renewable Energy Record", R.drawable.renew,
                "Renewables made up 65% of all new energy sources added globally in 2024, led by wind and solar.\n\n" +
                        "Germany, India, and the U.S. saw record-breaking installations of solar panels and offshore wind farms.\n\n" +
                        "The IEA predicts renewables will outpace fossil fuels entirely by 2040 if current growth continues."));

        list.add(new NewsItem("Space Tourism Takes Off", R.drawable.space,
                "Blue Origin and SpaceX both completed civilian missions, marking the start of regular space tourism.\n\n" +
                        "Tickets for suborbital trips are priced at $250,000, with reservations already booked for the next 2 years.\n\n" +
                        "Future plans include orbital hotels and lunar flybys, making space more accessible than ever."));

        list.add(new NewsItem("Virtual Reality Education", R.drawable.virt,
                "VR is transforming education by simulating labs, historical events, and collaborative environments.\n\n" +
                        "Teachers can guide students through virtual surgeries, spacewalks, or ancient ruins.\n\n" +
                        "Education ministries are investing in VR headsets for public schools to improve engagement and retention."));

        list.add(new NewsItem("Cybersecurity Startups Rise", R.drawable.cyb,
                "A new wave of startups is using AI to detect phishing, ransomware, and insider threats in real-time.\n\n" +
                        "Venture capital in the cybersecurity space hit $18 billion last quarter alone.\n\n" +
                        "Governments and enterprises are fast-tracking adoption of these tools in response to growing cyberattacks."));

        list.add(new NewsItem("Wildlife Conservation Win", R.drawable.wild,
                "Endangered species like the Amur Leopard and Black Rhino are seeing population recovery after years of decline.\n\n" +
                        "New sanctuaries and stricter anti-poaching laws are helping conservationists protect habitats.\n\n" +
                        "Awareness campaigns and community involvement have also played a crucial role in reversing the trends."));

        return list;
    }
}
