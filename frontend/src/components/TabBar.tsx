import styles from './TabBar.module.css'

interface Tab {
  id: string
  label: string
}

interface TabBarProps {
  tabs: Tab[]
  activeTab: string
  onTabChange: (id: string) => void
  ariaLabel?: string
}

export default function TabBar({ tabs, activeTab, onTabChange, ariaLabel = 'Navigation' }: TabBarProps) {
  return (
    <>
      <nav className={styles.tabBar} aria-label={ariaLabel}>
        {tabs.map(tab => (
          <button
            key={tab.id}
            type="button"
            className={`${styles.tab} ${tab.id === activeTab ? styles.tabActive : ''}`}
            onClick={() => onTabChange(tab.id)}
          >
            {tab.label}
          </button>
        ))}
      </nav>
      <div className={styles.tabSeparator} />
    </>
  )
}
