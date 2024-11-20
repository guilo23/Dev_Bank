import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  images: {
    localPatterns: [
      {
        pathname: '/public/images/**',
        search: '',
      },
    ],
  },
};

export default nextConfig;
