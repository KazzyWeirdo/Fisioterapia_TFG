import styles from './Breadcrumb.module.css'

export interface BreadcrumbItem {
  label: string
  onClick?: () => void
}

interface BreadcrumbProps {
  items: BreadcrumbItem[]
}

export default function Breadcrumb({ items }: BreadcrumbProps) {
  return (
    <nav className={styles.breadcrumb}>
      {items.map((item, i) => {
        const isLast = i === items.length - 1
        return (
          <span key={i} style={{ display: 'contents' }}>
            {isLast ? (
              <span className={styles.current}>{item.label}</span>
            ) : (
              <button type="button" className={styles.link} onClick={item.onClick}>
                {item.label}
              </button>
            )}
            {!isLast && <span className={styles.sep}>›</span>}
          </span>
        )
      })}
    </nav>
  )
}
