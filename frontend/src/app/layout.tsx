import type { Metadata } from 'next';

import './globals.css';

export const metadata: Metadata = {
  title: 'Budget Management',
  description: 'Application de gestion de budget personnel et partagé',
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="fr">
      <body>{children}</body>
    </html>
  );
}
