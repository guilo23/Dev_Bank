import Image from 'next/image';
import urubuPic from '@/public/images/urubu-pix.jpg';

export default function Home() {
  return (
    <div className="h-screen flex flex-col justify-center items-center">
      <h1 className="mb-4 font-extrabold text-3xl">Welcome to DevBank!</h1>
      <Image src={urubuPic} alt="Best Picture Ever" width={500} height={500} />
    </div>
  );
}
