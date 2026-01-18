import { Service } from '@/types/service';
import { motion } from 'framer-motion';

async function getServices(): Promise<Service[]> {
    const res = await fetch(`${process.env.API_URL}/api/services`);
    return res.json();
}

export default async function ServicesPage() {
    const services = await getServices();
    
    // Icons mapping (in real app, use react-icons)
    const iconMap: Record<string, string> = {
        'data-analysis': '📊',
        'software-dev': '💻',
        'research': '🔬',
        'consulting': '👥'
    };
    
    return (
        <div className="container mx-auto px-4 py-12">
            <motion.div 
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                className="text-center mb-12"
            >
                <h1 className="text-5xl font-bold text-blue-500 mb-4">
                    Professional Services
                </h1>
                <p className="text-xl text-gray-600 max-w-3xl mx-auto">
                    Beyond courses: we provide expert services in data analysis, 
                    software development, and scientific research.
                </p>
            </motion.div>
            
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                {services.map((service, index) => (
                    <motion.div
                        key={service.id}
                        initial={{ opacity: 0, scale: 0.9 }}
                        animate={{ opacity: 1, scale: 1 }}
                        transition={{ delay: index * 0.1 }}
                        className="bg-white rounded-2xl shadow-xl p-8 border border-purple-100 hover:border-purple-300 transition-colors"
                    >
                        <div className="text-5xl mb-6">
                        {service.icon ? iconMap[service.icon] ?? '✨' : '✨'}
                            </div>

                        <h3 className="text-2xl font-bold text-purple-500 mb-4">
                            {service.title}
                        </h3>
                        <p className="text-gray-600 mb-6">{service.description}</p>
                        <div className="flex items-center justify-between">
                            <span className="px-4 py-2 bg-orange-100 text-orange-500 rounded-full text-sm font-semibold">
                                {service.priceModel}
                            </span>
                            <button className="px-6 py-3 bg-linear-to-r from-blue-500 to-purple-500 text-white rounded-lg hover:opacity-90 transition">
                                Learn More
                            </button>
                        </div>
                    </motion.div>
                ))}
            </div>
        </div>
    );
}