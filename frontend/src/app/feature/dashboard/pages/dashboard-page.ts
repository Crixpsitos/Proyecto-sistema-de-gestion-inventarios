import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { DashboardModel } from '@/core/models/dashboard/Dashboard';
import { DashboardService } from '@/core/services/dashboard/dashboard';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [MatIconModule],
  templateUrl: './dashboard-page.html',
  styleUrl: './dashboard-page.css',
  providers: [DashboardService],
})
export class DashboardPage implements OnInit {
  private readonly dashboardService = inject(DashboardService);

  public readonly dashboard = signal<DashboardModel | null>(null);
  public readonly loading = signal(true);

  public readonly stockBalance = computed(() => {
    const metrics = this.dashboard()?.movementMetrics;
    if (!metrics) {
      return 0;
    }

    return metrics.last7DaysIn - metrics.last7DaysOut;
  });

  ngOnInit(): void {
    this.dashboardService.getDashboard().subscribe({
      next: (data) => {
        this.dashboard.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.dashboard.set(null);
        this.loading.set(false);
      },
    });
  }

  public formatDate(dateIso: string | null): string {
    if (!dateIso) {
      return 'Sin movimientos';
    }

    return new Intl.DateTimeFormat('es-CO', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    }).format(new Date(dateIso));
  }
}
