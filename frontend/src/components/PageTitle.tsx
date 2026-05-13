import styles from './PageTitle.module.css'

interface PageTitleProps {
  title: string
  subtitle?: string
}

export default function PageTitle({ title, subtitle }: PageTitleProps) {
  return (
    <>
      <h1 className={styles.heading}>{title}</h1>
      {subtitle && <p className={styles.subtitle}>{subtitle}</p>}
    </>
  )
}
